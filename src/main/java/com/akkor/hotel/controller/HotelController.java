package com.akkor.hotel.controller;

import com.akkor.hotel.dto.HotelRequest;
import com.akkor.hotel.dto.HotelResponse;
import com.akkor.hotel.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HotelController {

    private final HotelService hotelService;

    @GetMapping("/public")
    public ResponseEntity<List<HotelResponse>> getAllActiveHotels() {
        return ResponseEntity.ok(hotelService.getAllActiveHotels());
    }

    @GetMapping("/public/search")
    public ResponseEntity<List<HotelResponse>> searchHotels(
            @RequestParam String searchTerm) {
        return ResponseEntity.ok(hotelService.searchHotels(searchTerm));
    }

    @GetMapping("/public/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponse> createHotel(@RequestHeader("Authorization") String token,
            @RequestBody @Valid HotelRequest request) {
        System.out.println(token);
        return ResponseEntity.ok(hotelService.createHotel(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HotelResponse> updateHotel(
            @PathVariable Long id,
            @RequestBody @Valid HotelRequest request) {
        return ResponseEntity.ok(hotelService.updateHotel(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public/city/{city}")
    public ResponseEntity<List<HotelResponse>> getHotelsByCity(
            @PathVariable String city) {
        return ResponseEntity.ok(hotelService.getHotelsByCity(city));
    }

    @GetMapping("/public/country/{country}")
    public ResponseEntity<List<HotelResponse>> getHotelsByCountry(
            @PathVariable String country) {
        return ResponseEntity.ok(hotelService.getHotelsByCountry(country));
    }
} 