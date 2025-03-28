package com.litvas.booking.controller;

import com.litvas.booking.dto.BookingDTO;
import com.litvas.booking.dto.BookReservationDTO;
import com.litvas.booking.dto.BookingModificateDTO;
import com.litvas.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
@Tag(name = "Booking", description = "API for booking, paying, and cancelling reservations")
public class BookingController {

    private final BookingService bookingService;

    @Operation(summary = "Book a unit", description = "Creates a new booking for a specific unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successful",
                    content = @Content(schema = @Schema(implementation = BookReservationDTO.class))),
    })
    @PostMapping
    public BookReservationDTO bookUnits(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Booking data including unit ID and dates",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BookingDTO.class))
            )
            @RequestBody BookingDTO bookDTO) {
        return bookingService.bookUnit(bookDTO.getUnitId(), bookDTO.getCheckInDate(), bookDTO.getCheckOutDate());
    }

    @Operation(summary = "Cancel a booking", description = "Cancels an existing booking by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking successfully cancelled",
                    content = @Content(schema = @Schema(implementation = BookReservationDTO.class))),
    })
    @PutMapping("/cancel")
    public BookReservationDTO cancelBooking(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Booking cancellation data (booking ID)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BookingModificateDTO.class))
            )
            @RequestBody BookingModificateDTO bookDTO) {
        return bookingService.cancelBooking(bookDTO.getBookingId());
    }

    @Operation(summary = "Mark booking as paid", description = "Marks a specific booking as paid by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking marked as paid",
                    content = @Content(schema = @Schema(implementation = BookReservationDTO.class))),
    })
    @PutMapping("/paid")
    public BookReservationDTO paidBooking(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Booking payment data (booking ID)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BookingModificateDTO.class))
            )
            @RequestBody BookingModificateDTO bookDTO) {
        return bookingService.paidBooking(bookDTO.getBookingId());
    }
}
