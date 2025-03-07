package com.akkor.hotel.service;

import com.akkor.hotel.dto.HotelRequest;
import com.akkor.hotel.dto.HotelResponse;
import com.akkor.hotel.exception.ApiException;
import com.akkor.hotel.model.Hotel;
import com.akkor.hotel.repository.HotelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class  HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel hotel;
    private HotelRequest hotelRequest;

    @BeforeEach
    void setUp() {
        hotel = Hotel.builder()
                .id(1L)
                .name("Test Hotel")
                .address("123 Test St")
                .city("Test City")
                .country("Test Country")
                .basePrice(BigDecimal.valueOf(100))
                .totalRooms(10)
                .active(true)
                .build();

        hotelRequest = HotelRequest.builder()
                .name("Test Hotel")
                .address("123 Test St")
                .city("Test City")
                .country("Test Country")
                .basePrice(BigDecimal.valueOf(100))
                .totalRooms(10)
                .build();
    }

    @Test
    void getAllActiveHotels_ShouldReturnActiveHotels() {
        when(hotelRepository.findByActiveTrue()).thenReturn(Arrays.asList(hotel));

        List<HotelResponse> result = hotelService.getAllActiveHotels();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo(hotel.getName());
        verify(hotelRepository).findByActiveTrue();
    }

    @Test
    void getHotelById_WhenHotelExists_ShouldReturnHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));

        HotelResponse result = hotelService.getHotelById(1L);

        assertThat(result.getName()).isEqualTo(hotel.getName());
        verify(hotelRepository).findById(1L);
    }

    @Test
    void getHotelById_WhenHotelDoesNotExist_ShouldThrowException() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> hotelService.getHotelById(1L));
        verify(hotelRepository).findById(1L);
    }

    @Test
    void createHotel_ShouldCreateAndReturnHotel() {
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        HotelResponse result = hotelService.createHotel(hotelRequest);

        assertThat(result.getName()).isEqualTo(hotelRequest.getName());
        verify(hotelRepository).save(any(Hotel.class));
    }

    @Test
    void updateHotel_WhenHotelExists_ShouldUpdateAndReturnHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        HotelResponse result = hotelService.updateHotel(1L, hotelRequest);

        assertThat(result.getName()).isEqualTo(hotelRequest.getName());
        verify(hotelRepository).findById(1L);
        verify(hotelRepository).save(any(Hotel.class));
    }

    @Test
    void updateHotel_WhenHotelDoesNotExist_ShouldThrowException() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> hotelService.updateHotel(1L, hotelRequest));
        verify(hotelRepository).findById(1L);
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void deleteHotel_WhenHotelExists_ShouldDeactivateHotel() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        hotelService.deleteHotel(1L);

        verify(hotelRepository).findById(1L);
        verify(hotelRepository).save(any(Hotel.class));
    }

    @Test
    void deleteHotel_WhenHotelDoesNotExist_ShouldThrowException() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> hotelService.deleteHotel(1L));
        verify(hotelRepository).findById(1L);
        verify(hotelRepository, never()).save(any(Hotel.class));
    }
} 