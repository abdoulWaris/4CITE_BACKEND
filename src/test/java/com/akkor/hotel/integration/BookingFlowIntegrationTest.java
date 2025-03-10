package com.akkor.hotel.integration;

import com.akkor.hotel.dto.*;
import com.akkor.hotel.model.BookingStatus;
import com.akkor.hotel.model.Role;
import com.akkor.hotel.model.User;
import com.akkor.hotel.repository.BookingRepository;
import com.akkor.hotel.repository.HotelRepository;
import com.akkor.hotel.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.yml")
@TestPropertySource(properties = "SPRING_JWT_SECRET_TOKEN=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970")
class BookingFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        hotelRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    void completeBookingFlow() throws Exception {
        // 1. Register a new user
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test@test.com")
                .password("Test123@")
                .firstName("Test1")
                .lastName("User1")
                .build();

        // Using MockMvc to perform the POST request
        MvcResult registerResult = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        AuthenticationResponse authResponse = objectMapper.readValue(
                registerResult.getResponse().getContentAsString(),
                AuthenticationResponse.class);
        String token = "Bearer " + authResponse.getToken();
        java.util.List<User> users = userRepository.findAll();


        /**
         * etape de connexion
         */
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email(users.get(0).getEmail())
                .password("Test123@")
                .build();

        MvcResult auth = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andReturn();

        AuthenticationResponse authResp = objectMapper.readValue(
                auth.getResponse().getContentAsString(),
                AuthenticationResponse.class);
       // System.out.println("deuxieme token" +authResp.getToken());

        // 2. Create a new hotel (as admin)
        HotelRequest hotelRequest = HotelRequest.builder()
                .name("Test Hotel")
                .address("123 Test St")
                .city("Test City")
                .country("Test Country")
                .basePrice(BigDecimal.valueOf(100))
                .totalRooms(10)
                .build();

        MvcResult hotelFirstTestResult = mockMvc.perform(post("/api/hotels/create")
                .header("Authorization", "Bearer "+authResp.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(hotelRequest)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

} 