package com.akkor.hotel.integration;

import com.akkor.hotel.dto.*;
import com.akkor.hotel.model.BookingStatus;
import com.akkor.hotel.model.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookingFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void completeBookingFlow() throws Exception {
        // 1. Register a new user
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test@test.com")
                .password("Test123@")
                .firstName("Test")
                .lastName("User")
                .build();

        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthenticationResponse authResponse = objectMapper.readValue(
                registerResult.getResponse().getContentAsString(),
                AuthenticationResponse.class);
        String token = "Bearer " + authResponse.getToken();

        // 2. Create a new hotel (as admin)
        HotelRequest hotelRequest = HotelRequest.builder()
                .name("Test Hotel")
                .address("123 Test St")
                .city("Test City")
                .country("Test Country")
                .basePrice(BigDecimal.valueOf(100))
                .totalRooms(10)
                .build();

        MvcResult hotelResult = mockMvc.perform(post("/api/hotels")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hotelRequest)))
                .andExpect(status().isOk())
                .andReturn();

        HotelResponse hotelResponse = objectMapper.readValue(
                hotelResult.getResponse().getContentAsString(),
                HotelResponse.class);

        // 3. Create a booking
        BookingRequest bookingRequest = BookingRequest.builder()
                .hotelId(hotelResponse.getId())
                .checkInDate(LocalDate.now().plusDays(1))
                .checkOutDate(LocalDate.now().plusDays(3))
                .numberOfGuests(2)
                .build();

        MvcResult bookingResult = mockMvc.perform(post("/api/bookings")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andReturn();

        BookingResponse bookingResponse = objectMapper.readValue(
                bookingResult.getResponse().getContentAsString(),
                BookingResponse.class);

        assertThat(bookingResponse.getStatus()).isEqualTo(BookingStatus.PENDING);

        // 4. Get booking details
        mockMvc.perform(get("/api/bookings/" + bookingResponse.getId())
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()));

        // 5. Cancel booking
        mockMvc.perform(put("/api/bookings/" + bookingResponse.getId() + "/cancel")
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }
} 