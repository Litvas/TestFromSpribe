package com.litvas.booking.service;

import com.litvas.booking.domain.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UnitService {

    Page<Unit> searchAvailableUnits(BigDecimal cost, LocalDate checkInDate, LocalDate checkOutDate, Pageable pageable);


}
