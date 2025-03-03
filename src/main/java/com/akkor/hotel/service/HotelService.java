package com.akkor.hotel.service;

import com.akkor.hotel.dto.HotelRequest;
import com.akkor.hotel.dto.HotelResponse;
import com.akkor.hotel.exception.ApiException;
import com.akkor.hotel.model.Hotel;
import com.akkor.hotel.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    public List<HotelResponse> getAllActiveHotels() {
        return hotelRepository.findByActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public HotelResponse getHotelById(Long id) {
        return hotelRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));
    }

    @Transactional
    public HotelResponse createHotel(HotelRequest request) {
        Hotel hotel = mapToEntity(request);
        hotel.setActive(true);
        hotel = hotelRepository.save(hotel);
        return mapToResponse(hotel);
    }

    @Transactional
    public HotelResponse updateHotel(Long id, HotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));
        
        updateHotelFromRequest(hotel, request);
        hotel = hotelRepository.save(hotel);
        return mapToResponse(hotel);
    }

    @Transactional
    public void deleteHotel(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new ApiException("Hotel not found", HttpStatus.NOT_FOUND));
        hotel.setActive(false);
        hotelRepository.save(hotel);
    }

    public List<HotelResponse> searchHotels(String searchTerm) {
        return hotelRepository.searchHotels(searchTerm).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<HotelResponse> getHotelsByCity(String city) {
        return hotelRepository.findByCityIgnoreCaseAndActiveTrue(city).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<HotelResponse> getHotelsByCountry(String country) {
        return hotelRepository.findByCountryIgnoreCaseAndActiveTrue(country).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private Hotel mapToEntity(HotelRequest request) {
        return Hotel.builder()
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

    private void updateHotelFromRequest(Hotel hotel, HotelRequest request) {
        hotel.setName(request.getName());
        hotel.setAddress(request.getAddress());
        hotel.setCity(request.getCity());
        hotel.setCountry(request.getCountry());
        hotel.setDescription(request.getDescription());
        hotel.setImageUrls(request.getImageUrls());
        hotel.setBasePrice(request.getBasePrice());
        hotel.setTotalRooms(request.getTotalRooms());
        hotel.setContactEmail(request.getContactEmail());
        hotel.setContactPhone(request.getContactPhone());
        hotel.setStarRating(request.getStarRating());
        hotel.setAmenities(request.getAmenities());
    }

    public HotelResponse mapToResponse(Hotel hotel) {
        return HotelResponse.builder()
                .id(hotel.getId())
                .name(hotel.getName())
                .address(hotel.getAddress())
                .city(hotel.getCity())
                .country(hotel.getCountry())
                .description(hotel.getDescription())
                .imageUrls(hotel.getImageUrls())
                .basePrice(hotel.getBasePrice())
                .totalRooms(hotel.getTotalRooms())
                .active(hotel.isActive())
                .contactEmail(hotel.getContactEmail())
                .contactPhone(hotel.getContactPhone())
                .starRating(hotel.getStarRating())
                .amenities(hotel.getAmenities())
                .build();
    }
} 