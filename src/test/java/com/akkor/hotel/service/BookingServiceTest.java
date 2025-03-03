package com.akkor.hotel.service;

import com.akkor.hotel.dto.BookingRequest;
import com.akkor.hotel.dto.BookingResponse;
import com.akkor.hotel.exception.ApiException;
import com.akkor.hotel.model.*;
import com.akkor.hotel.repository.BookingRepository;
import com.akkor.hotel.repository.HotelRepository;
import com.akkor.hotel.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelService hotelService;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private Hotel hotel;
    private Booking booking;
    private BookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@test.com")
                .firstName("Test")
                .lastName("User")
                .build();

        hotel = Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .basePrice(BigDecimal.valueOf(100))
                .totalRooms(10)
                .active(true)
                .build();

        booking = Booking.builder()
                .id(1L)
                .user(user)
                .hotel(hotel)
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(3))
                .numberOfGuests(2)
                .totalPrice(BigDecimal.valueOf(200))
                .status(BookingStatus.PENDING)
                .build();

        bookingRequest = BookingRequest.builder()
                .hotelId(1L)
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(3))
                .numberOfGuests(2)
                .build();
    }

    @Test
    void createBooking_WhenValidRequest_ShouldCreateBooking() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(hotelService.mapToResponse(any())).thenReturn(null);

        BookingResponse response = bookingService.createBooking(bookingRequest, "test@test.com");

        assertThat(response).isNotNull();
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void createBooking_WhenNoRoomsAvailable_ShouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.of(hotel));
        when(bookingRepository.findOverlappingBookings(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(booking, booking, booking));

        assertThrows(ApiException.class,
                () -> bookingService.createBooking(bookingRequest, "test@test.com"));
    }

    @Test
    void getUserBookings_ShouldReturnUserBookings() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(bookingRepository.findByUserId(anyLong())).thenReturn(Arrays.asList(booking));
        when(hotelService.mapToResponse(any())).thenReturn(null);

        List<BookingResponse> responses = bookingService.getUserBookings("test@test.com");

        assertThat(responses).hasSize(1);
        verify(bookingRepository).findByUserId(user.getId());
    }

    @Test
    void getBookingById_WhenAuthorized_ShouldReturnBooking() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(hotelService.mapToResponse(any())).thenReturn(null);

        BookingResponse response = bookingService.getBookingById(1L, "test@test.com");

        assertThat(response).isNotNull();
        verify(bookingRepository).findById(1L);
    }

    @Test
    void getBookingById_WhenUnauthorized_ShouldThrowException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(ApiException.class,
                () -> bookingService.getBookingById(1L, "other@test.com"));
    }

    @Test
    void updateBookingStatus_WhenAuthorized_ShouldUpdateStatus() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(hotelService.mapToResponse(any())).thenReturn(null);

        BookingResponse response = bookingService.updateBookingStatus(
                1L, "test@test.com", BookingStatus.CANCELLED);

        assertThat(response).isNotNull();
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void updateBookingStatus_WhenUnauthorized_ShouldThrowException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(ApiException.class,
                () -> bookingService.updateBookingStatus(1L, "other@test.com", BookingStatus.CANCELLED));
    }
} 