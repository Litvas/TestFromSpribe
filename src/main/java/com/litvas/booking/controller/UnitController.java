package com.litvas.booking.controller;

import com.litvas.booking.domain.Unit;
import com.litvas.booking.service.UnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/units")
@Tag(name = "Units", description = "API for searching available units")
public class UnitController {

    private final UnitService unitService;

    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @Operation(
            summary = "Search for available units",
            description = "Returns a list of available units filtered by price and dates"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful response with available units"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public Page<Unit> searchAvailableUnits(
            @Parameter(description = "Maximum price", example = "150.00")
            @RequestParam BigDecimal price,
            @Parameter(description = "Check-in date (yyyy-MM-dd)", example = "2025-04-01")
            @RequestParam LocalDate checkInDate,
            @Parameter(description = "Check-out date (yyyy-MM-dd)", example = "2025-04-05")
            @RequestParam LocalDate checkOutDate,
            @Parameter(description = "Pagination information. Number of page")
            @RequestParam(required = false, defaultValue = "0") int pageNumber,
            @Parameter(description = "Pagination information. Quantity of entries")
            @RequestParam(required = false, defaultValue = "10") int pageSize
    ) {
        return unitService.searchAvailableUnits(price, checkInDate, checkOutDate, PageRequest.of(pageNumber, pageSize));
    }
}
