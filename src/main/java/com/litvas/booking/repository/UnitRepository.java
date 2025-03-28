package com.litvas.booking.repository;

import com.litvas.booking.domain.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UnitRepository extends JpaRepository<Unit, Long> {

    @Query("""
                SELECT u FROM Unit u
                WHERE (:price IS NULL OR u.price <= :price)
                AND NOT EXISTS (
                    SELECT b FROM Booking b
                    WHERE b.unit = u
                    AND b.checkInDate < :checkOutDate
                    AND b.checkOutDate > :checkInDate
                )
            """)
    Page<Unit> findAvailableUnits(
            @Param("price") BigDecimal price,
            @Param("checkInDate") LocalDate checkInDate,
            @Param("checkOutDate") LocalDate checkOutDate,
            Pageable pageable
    );
}
