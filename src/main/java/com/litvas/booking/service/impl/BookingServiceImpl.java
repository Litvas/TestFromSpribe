package com.litvas.booking.service.impl;

import com.litvas.booking.domain.Booking;
import com.litvas.booking.domain.Unit;
import com.litvas.booking.dto.BookReservationDTO;
import com.litvas.booking.mapper.BookingMapper;
import com.litvas.booking.repository.BookingRepository;
import com.litvas.booking.repository.UnitRepository;
import com.litvas.booking.service.BookingService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookingServiceImpl implements BookingService {

    private final UnitRepository unitRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Value("${app.properties.booking-percent}")
    private BigDecimal bookingPercent;

    @Value("${app.properties.time-threshold}")
    private Long timeThreshold;

    @Override
    @Transactional
    @CacheEvict(value = "booking", allEntries = true)
    public BookReservationDTO bookUnit(Long unitId, LocalDate checkInDate, LocalDate checkOutDate) {
        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(
                        "Unit with id='%s' not found", unitId)));

        Booking booking = Booking.builder()
                .unit(unit)
                .checkInDate(checkInDate)
                .checkOutDate(checkOutDate)
                .createdAt(LocalDateTime.now())
                .isPaid(false)
                .price(unit.getPrice().add(unit.getPrice().multiply(bookingPercent)))
                .build();
        bookingRepository.save(booking);
        log.info("Unit with id={} was booked", unitId);
        return bookingMapper.mapBooking(booking);
    }

    @Override
    @Transactional
    @CacheEvict(value = "booking", allEntries = true)
    public BookReservationDTO cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        booking.setIsCanceled(true);
        return bookingMapper.mapBooking(booking);
    }

    @Override
    @Transactional
    @Cacheable(value = "booking", key = "#bookingId")
    public BookReservationDTO paidBooking(Long bookingId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        booking.setIsPaid(true);
        bookingRepository.save(booking);
        return bookingMapper.mapBooking(booking);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    @CacheEvict(value = "booking", allEntries = true)
    public void cancelUnpaidBookings() {
        LocalDateTime someMinutesAgo = LocalDateTime.now().minusMinutes(timeThreshold);
        List<Booking> unpaidBookings = bookingRepository
                .findRecentUnpaidBookings(someMinutesAgo, false, false);
        unpaidBookings.forEach(entry -> entry.setIsCanceled(true));
        bookingRepository.saveAll(unpaidBookings);
    }

}
