package com.litvas.booking.controller;

import com.litvas.booking.dto.BookingDTO;
import com.litvas.booking.dto.BookReservationDTO;
import com.litvas.booking.dto.BookingModificateDTO;
import com.litvas.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookReservationDTO bookUnits(@RequestBody BookingDTO bookDTO) {
        return bookingService.bookUnit(bookDTO.getUnitId(), bookDTO.getCheckInDate(), bookDTO.getCheckOutDate() );
    }

    @PutMapping("/cancel")
    public BookReservationDTO cancelBooking(@RequestBody BookingModificateDTO bookDTO) {
        return bookingService.cancelBooking(bookDTO.getBookingId());
    }

    @PutMapping("/paid")
    public BookReservationDTO paidBooking(@RequestBody BookingModificateDTO bookDTO) {
        return bookingService.paidBooking(bookDTO.getBookingId());
    }

}
