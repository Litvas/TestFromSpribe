package com.litvas.booking.service;
import com.litvas.booking.domain.Booking;
import com.litvas.booking.domain.Unit;
import com.litvas.booking.dto.BookReservationDTO;
import com.litvas.booking.mapper.BookingMapper;
import com.litvas.booking.repository.BookingRepository;
import com.litvas.booking.repository.UnitRepository;
import com.litvas.booking.service.impl.BookingServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private UnitRepository unitRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Unit unit;
    private Booking booking;
    private BookReservationDTO bookReservationDTO;

    @BeforeEach
    void setUp() {
        unit = new Unit();
        unit.setId(1L);
        unit.setPrice(BigDecimal.valueOf(100));

        booking = new Booking();
        booking.setId(1L);
        booking.setUnit(unit);
        booking.setCheckInDate(LocalDate.now());
        booking.setCheckOutDate(LocalDate.now().plusDays(3));
        booking.setCreatedAt(LocalDateTime.now());
        booking.setIsPaid(false);
        booking.setIsCanceled(false);
        booking.setPrice(BigDecimal.valueOf(110));

        bookReservationDTO = new BookReservationDTO();

        ReflectionTestUtils.setField(bookingService, "bookingPercent", BigDecimal.valueOf(0.1));
        ReflectionTestUtils.setField(bookingService, "timeThreshold", 60L);
    }

    @Test
    void bookUnit_Success() {
        when(unitRepository.findById(1L)).thenReturn(Optional.of(unit));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.mapBooking(any())).thenReturn(bookReservationDTO);

        BookReservationDTO result = bookingService.bookUnit(1L, LocalDate.now(), LocalDate.now().plusDays(3));

        assertNotNull(result);
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void bookUnit_ThrowsException_WhenUnitNotFound() {
        when(unitRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                bookingService.bookUnit(1L, LocalDate.now(), LocalDate.now().plusDays(3)));
    }

    @Test
    void cancelBooking_Success() {
        when(bookingRepository.getReferenceById(1L)).thenReturn(booking);
        when(bookingMapper.mapBooking(any())).thenReturn(bookReservationDTO);

        BookReservationDTO result = bookingService.cancelBooking(1L);

        assertNotNull(result);
        assertTrue(booking.getIsCanceled());
    }

    @Test
    void cancelBooking_ThrowsException_WhenBookingNotFound() {
        when(bookingRepository.getReferenceById(1L)).thenThrow(new EntityNotFoundException("Booking not found"));

        assertThrows(EntityNotFoundException.class, () -> bookingService.cancelBooking(1L));
    }

    @Test
    void paidBooking_Success() {
        when(bookingRepository.getReferenceById(1L)).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.mapBooking(any())).thenReturn(bookReservationDTO);

        BookReservationDTO result = bookingService.paidBooking(1L);

        assertNotNull(result);
        assertTrue(booking.getIsPaid());
    }

    @Test
    void paidBooking_ThrowsException_WhenBookingNotFound() {
        when(bookingRepository.getReferenceById(1L)).thenThrow(new EntityNotFoundException("Booking not found"));

        assertThrows(EntityNotFoundException.class, () -> bookingService.paidBooking(1L));
    }

    @Test
    void cancelUnpaidBookings_Success() {
        when(bookingRepository.findRecentUnpaidBookings(any(), eq(false), eq(false)))
                .thenReturn(List.of(booking));

        bookingService.cancelUnpaidBookings();

        assertTrue(booking.getIsCanceled());
        verify(bookingRepository, times(1)).saveAll(anyList());
    }

    @Test
    void cancelUnpaidBookings_NoBookingsToCancel() {
        when(bookingRepository.findRecentUnpaidBookings(any(), eq(false), eq(false)))
                .thenReturn(List.of());

        bookingService.cancelUnpaidBookings();

        verify(bookingRepository, times(1)).saveAll(anyList());
    }
}

