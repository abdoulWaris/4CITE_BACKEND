package com.akkor.hotel.repository;

import com.akkor.hotel.dto.BookingRequest;
import com.akkor.hotel.model.Booking;
import com.akkor.hotel.model.BookingStatus;
import com.akkor.hotel.model.Hotel;
import com.akkor.hotel.model.User;
import com.akkor.hotel.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = "SPRING_APPLICATION_NAME=TestApp")
class BookingRepositoryTest {

    Hotel hotel;
    Booking booking;
    User user;
    BookingRequest bookingRequest;

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HotelRepository hotelRepository;
    @MockBean
    HotelService hotelService;

    @BeforeEach
    void setUp() {
        user = userRepository.findById(1L).get();

        hotel = hotelRepository.findById(1L).get();

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
    void shouldGetAllBookings() {
        java.util.List <Booking> listOfBooking = bookingRepository.findAll();
        assertEquals(3,listOfBooking.size());
        //assertEquals("John",listOfBooking.get(1).getUser().getFirstName());
    }

    @Test
    void shouldCreateABooking(){
        Booking savedBooking = bookingRepository.save(booking);
        Optional<Booking> optionalBooking = bookingRepository.findById(savedBooking.getId());
        //java.util.List <Booking> listOfBooking = bookingRepository.findAll();
        assertTrue(optionalBooking.isPresent());
        assertNotNull(savedBooking.getId());
    }

    @Test
    void shouldUpdateABooking(){
        Booking savedBooking = bookingRepository.save(booking);
        Optional<Booking> optionalBooking = bookingRepository.findById(3L);
        Booking findABooking = bookingRepository.findById(2L).orElseThrow(null);

        findABooking.setStatus(BookingStatus.CONFIRMED);
        savedBooking.setStatus(BookingStatus.CANCELLED);

        Booking updatedBooking = bookingRepository.save(savedBooking);
        Booking updateFundBooking = bookingRepository.save(findABooking);

        assertEquals(BookingStatus.CONFIRMED,updateFundBooking.getStatus());
        assertEquals(BookingStatus.CANCELLED,updatedBooking.getStatus());
        assertTrue(optionalBooking.isPresent());
    }

    @Test
    void shouldDeleteABooking(){
        Booking savedBooking = bookingRepository.save(booking);
        bookingRepository.deleteById(savedBooking.getId());
        Optional<Booking> optionalBooking = bookingRepository.findById(savedBooking.getId());
        assertFalse(optionalBooking.isPresent());
    }

    @Test
    void shouldGetBookingById(){
        Booking booking1 = bookingRepository.findById(1L).get();
        Booking booking2 = bookingRepository.findById(2L).get();
        assertEquals("John",booking1.getUser().getFirstName());
        assertEquals("Jane",booking2.getUser().getFirstName());
    }
}