package com.litvas.booking.service;

import com.litvas.booking.domain.Booking;
import com.litvas.booking.dto.BookReservationDTO;

import java.time.LocalDate;

public interface BookingService {

    BookReservationDTO bookUnit(Long unitId, LocalDate checkInDate, LocalDate checkOutDate);
    BookReservationDTO cancelBooking(Long bookingId);
    BookReservationDTO paidBooking(Long bookingId);

}

