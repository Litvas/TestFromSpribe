package com.litvas.booking.controller;

import com.litvas.booking.domain.Unit;
import com.litvas.booking.service.UnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @GetMapping
    public Page<Unit> findUnits(@RequestParam(required = false) BigDecimal price,
                                @RequestParam LocalDate checkInDate,
                                @RequestParam LocalDate checkOutDate,
                                @RequestParam(required = false, defaultValue = "0") int pageNumber,
                                @RequestParam(required = false, defaultValue = "10") int pageSize) {
        return unitService.searchAvailableUnits(price, checkInDate, checkOutDate, PageRequest.of(pageNumber, pageSize));
    }

}
