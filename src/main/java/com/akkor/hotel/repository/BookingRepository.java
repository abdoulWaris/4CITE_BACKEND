package com.akkor.hotel.repository;

import com.akkor.hotel.model.Booking;
import com.akkor.hotel.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId);
    
    List<Booking> findByHotelId(Long hotelId);
    
    List<Booking> findByStatus(BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.hotel.id = :hotelId AND " +
           "((b.checkInDate BETWEEN :startDate AND :endDate) OR " +
           "(b.checkOutDate BETWEEN :startDate AND :endDate) OR " +
           "(b.checkInDate <= :startDate AND b.checkOutDate >= :endDate)) AND " +
           "b.status NOT IN ('CANCELLED', 'REFUNDED')")
    List<Booking> findOverlappingBookings(Long hotelId, LocalDate startDate, LocalDate endDate);
    
    List<Booking> findByUserIdAndStatus(Long userId, BookingStatus status);
} 