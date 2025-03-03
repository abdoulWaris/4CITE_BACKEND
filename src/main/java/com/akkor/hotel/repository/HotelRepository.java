package com.akkor.hotel.repository;

import com.akkor.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByActiveTrue();
    
    List<Hotel> findByCityIgnoreCaseAndActiveTrue(String city);
    
    List<Hotel> findByCountryIgnoreCaseAndActiveTrue(String country);
    
    @Query("SELECT h FROM Hotel h WHERE " +
           "LOWER(h.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(h.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(h.country) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND " +
           "h.active = true")
    List<Hotel> searchHotels(String searchTerm);
} 