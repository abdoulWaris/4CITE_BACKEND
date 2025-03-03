package com.akkor.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class HotelRequest {
    
    @NotBlank(message = "Hotel name is required")
    private String name;
    
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "Country is required")
    private String country;
    
    private String description;
    
    private List<String> imageUrls = new ArrayList<>();
    
    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;
    
    @NotNull(message = "Total rooms is required")
    @Positive(message = "Total rooms must be positive")
    private Integer totalRooms;
    
    private String contactEmail;
    
    private String contactPhone;
    
    private Integer starRating;
    
    private List<String> amenities = new ArrayList<>();
} 