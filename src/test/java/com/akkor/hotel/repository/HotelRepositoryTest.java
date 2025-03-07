package com.akkor.hotel.repository;

import com.akkor.hotel.dto.HotelRequest;
import com.akkor.hotel.model.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(properties = "SPRING_APPLICATION_NAME=TestApp")
class HotelRepositoryTest {
    @Autowired
    HotelRepository hotelRepository;

    private Hotel hotel;
    private HotelRequest request;

    @BeforeEach
    public void setUp() {
        request = new HotelRequest();
        request.setName("Sample Hotel");
        request.setAddress("123 Sample St");
        request.setCity("Sample City");
        request.setCountry("Sample Country");
        request.setDescription("A beautiful hotel for all your needs.");
        request.setImageUrls(Arrays.asList("url1.jpg", "url2.jpg"));
        request.setBasePrice(BigDecimal.valueOf(100.0));
        request.setTotalRooms(50);
        request.setContactEmail("contact@samplehotel.com");
        request.setContactPhone("+1234567890");
        request.setStarRating(4);
        request.setAmenities(Arrays.asList("Pool", "Gym", "Free Wi-Fi"));

        hotel = Hotel.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .description(request.getDescription())
                .imageUrls(request.getImageUrls())
                .basePrice(request.getBasePrice())
                .totalRooms(request.getTotalRooms())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .starRating(request.getStarRating())
                .amenities(request.getAmenities())
                .build();
    }


    @Test
    void shouldGetAllHotel(){
       java.util.List <Hotel> listOfHotel = hotelRepository.findAll();
        assertEquals(4,listOfHotel.size());
        assertEquals("Hotel London",listOfHotel.get(3).getName());
    }
    @Test
    void shouldCreateAnHotel(){

    Hotel savedHotel = hotelRepository.save(hotel);
    assertThat(savedHotel).isNotNull();
    assertThat(savedHotel.getId()).isGreaterThan(0);
    assertThat(savedHotel.getContactEmail()).isEqualTo("contact@samplehotel.com");
    assertThat(savedHotel.getDescription()).isEqualTo("A beautiful hotel for all your needs.");
    assertThat(savedHotel.getAmenities().get(2)).isEqualTo( "Free Wi-Fi");
    }
    @Test
    void shouldGetHotelByID(){
        Hotel hotel1 = hotelRepository.findById(1L).get();
        Hotel hotel2 = hotelRepository.findById(2L).get();
        assertEquals("Hotel Paris",hotel1.getName());
        assertEquals("Hotel New York",hotel2.getName());
    }

    @Test
    void ShouldUpdateHotel(){
        Hotel hotel1 = hotelRepository.findById(1L).get();
        hotel1.setName("Hotel KUALA LUMPUR");
        Hotel updatedHotel = hotelRepository.save(hotel1);
        assertEquals("Hotel KUALA LUMPUR",updatedHotel.getName());
    }

    @Test
    void shouldDeleteHotel(){
        Hotel hotel1 = hotelRepository.findById(1L).get();
        hotelRepository.delete(hotel1);
        Optional<Hotel> hotel = hotelRepository.findById(1L);
        assertFalse(hotel.isPresent());
    }

    @Test
    void shouldGetHotelByCity(){
        java.util.List <Hotel> listOfHotel = hotelRepository.findByCityIgnoreCaseAndActiveTrue("Paris");
        assertEquals(1,listOfHotel.size());
        assertEquals("Hotel Paris",listOfHotel.get(0).getName());
    }

    @Test
    void shouldGetHotelByCountry(){
        java.util.List <Hotel> listOfHotel = hotelRepository.findByCountryIgnoreCaseAndActiveTrue("France");
        assertEquals(1,listOfHotel.size());
        assertNotNull(listOfHotel.get(0).getCity());
    }
    @Test
    void shouldGetHotelBySearchTerm(){
        java.util.List <Hotel> listOfHotel = hotelRepository.searchHotels("Hotel");
        Optional<Hotel>hotelList = hotelRepository.findById(4L);
        assertEquals(4,listOfHotel.size());
        assertTrue(listOfHotel.contains(hotelList.get()));
        assertEquals("Hotel Sydney",listOfHotel.get(2).getName());
    }

    @Test
    void shouldGetHotelBySearchTermAndCity(){
        java.util.List <Hotel> listOfHotel = hotelRepository.searchHotels("Paris");
        assertEquals(1,listOfHotel.size());
        assertEquals("Hotel Paris",listOfHotel.get(0).getName());
    }

    @Test
    void shouldGetHotelBySearchTermAndCountry(){
        java.util.List <Hotel> listOfHotel = hotelRepository.searchHotels("France");

        assertEquals(1,listOfHotel.size());
        assertEquals("Hotel Paris",listOfHotel.get(0).getName());
        assertEquals("France",listOfHotel.get(0).getCountry());
    }

}