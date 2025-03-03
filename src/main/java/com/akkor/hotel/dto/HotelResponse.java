package com.akkor.hotel.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String country;
    private String description;
    private List<String> imageUrls = new ArrayList<>();
    private BigDecimal basePrice;
    private Integer totalRooms;
    private boolean active;
    private String contactEmail;
    private String contactPhone;
    private Integer starRating;
    private List<String> amenities = new ArrayList<>();
} 