package com.akkor.hotel.service;

import com.akkor.hotel.dto.BookingRequest;
import com.akkor.hotel.dto.BookingResponse;
import com.akkor.hotel.exception.ApiException;
import com.akkor.hotel.model.Booking;
import com.akkor.hotel.model.BookingStatus;
import com.akkor.hotel.model.Hotel;
import com.akkor.hotel.model.User;
import com.akkor.hotel.repository.BookingRepository;
import com.akkor.hotel.repository.HotelRepository;
import com.akkor.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final HotelService hotelService;

    @Transactional
    public BookingResponse createBooking(BookingRequest request, String userEmail) {
        validateBookingDates(request.getCheckInDate(), request.getCheckOutDate());
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));

        validateRoomAvailability(hotel.getId(), request.getCheckInDate(), request.getCheckOutDate());

        Booking booking = Booking.builder()
                .user(user)
                .hotel(hotel)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .numberOfGuests(request.getNumberOfGuests())
                .totalPrice(calculateTotalPrice(hotel.getBasePrice(), request.getCheckInDate(), request.getCheckOutDate()))
                .status(BookingStatus.PENDING)
                .specialRequests(request.getSpecialRequests())
                .build();

        booking = bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    public List<BookingResponse> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        
        return bookingRepository.findByUserId(user.getId()).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public BookingResponse getBookingById(Long id, String userEmail) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ApiException("Booking not found", HttpStatus.NOT_FOUND));
        
        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new ApiException("Unauthorized access to booking", HttpStatus.FORBIDDEN);
        }
        
        return mapToResponse(booking);
    }

    @Transactional
    public BookingResponse updateBookingStatus(Long id, String userEmail, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ApiException("Booking not found", HttpStatus.NOT_FOUND));
        
        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new ApiException("Unauthorized access to booking", HttpStatus.FORBIDDEN);
        }
        
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new ApiException("Booking cannot be modified in current status", HttpStatus.BAD_REQUEST);
        }
        
        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByHotel(Long hotelId) {
        return bookingRepository.findByHotelId(hotelId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse updateBookingStatusByAdmin(Long id, BookingStatus status) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ApiException("Booking not found", HttpStatus.NOT_FOUND));
        
        booking.setStatus(status);
        booking = bookingRepository.save(booking);
        return mapToResponse(booking);
    }

    private void validateBookingDates(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn.isAfter(checkOut)) {
            throw new ApiException("Check-in date must be before check-out date", HttpStatus.BAD_REQUEST);
        }
        
        if (checkIn.isBefore(LocalDate.now())) {
            throw new ApiException("Check-in date cannot be in the past", HttpStatus.BAD_REQUEST);
        }
    }

    private void validateRoomAvailability(Long hotelId, LocalDate checkIn, LocalDate checkOut) {
        List<Booking> overlappingBookings = bookingRepository.findOverlappingBookings(hotelId, checkIn, checkOut);
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));
        
        if (overlappingBookings.size() >= hotel.getTotalRooms()) {
            throw new ApiException("No rooms available for the selected dates", HttpStatus.BAD_REQUEST);
        }
    }

    private BigDecimal calculateTotalPrice(BigDecimal basePrice, LocalDate checkIn, LocalDate checkOut) {
        long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
        return basePrice.multiply(BigDecimal.valueOf(nights));
    }

    private BookingResponse mapToResponse(Booking booking) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getUser().getId())
                .userEmail(booking.getUser().getEmail())
                .userName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName())
                .hotel(hotelService.mapToResponse(booking.getHotel()))
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .numberOfGuests(booking.getNumberOfGuests())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .specialRequests(booking.getSpecialRequests())
                .build();
    }
} 