package com.litvas.booking.repository;

import com.litvas.booking.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b " +
            "WHERE b.createdAt <= :timeThreshold " +
            "AND (:is_paid IS NULL OR b.isPaid = :is_paid) " +
            "AND (b.isCanceled IS NULL OR b.isCanceled = :is_canceled)")
    List<Booking> findRecentUnpaidBookings(
            @Param("timeThreshold") LocalDateTime timeThreshold,
            @Param("is_paid") Boolean isPaid,
            @Param("is_canceled") Boolean isCanceled
    );

}
