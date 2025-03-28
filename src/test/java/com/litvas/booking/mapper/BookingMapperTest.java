package com.litvas.booking.mapper;

import com.litvas.booking.domain.Booking;
import com.litvas.booking.domain.Client;
import com.litvas.booking.domain.Unit;
import com.litvas.booking.domain.AccommodationType;
import com.litvas.booking.dto.BookReservationDTO;
import com.litvas.booking.dto.UnitDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    void setUp() {
        bookingMapper = Mappers.getMapper(BookingMapper.class);
    }

    @Test
    void mapBooking_ShouldMapAllFieldsCorrectly() {
        Unit unit = Unit.builder()
                .id(42L)
                .price(BigDecimal.valueOf(120))
                .rooms(2)
                .floor(3)
                .type(AccommodationType.APARTMENTS)
                .build();

        Client client = Client.builder()
                .id(7L)
                .name("John Doe")
                .email("john@example.com")
                .build();

        Booking booking = Booking.builder()
                .id(1L)
                .unit(unit)
                .client(client)
                .checkInDate(LocalDate.of(2025, 4, 1))
                .checkOutDate(LocalDate.of(2025, 4, 5))
                .createdAt(LocalDateTime.now())
                .isPaid(true)
                .build();

        BookReservationDTO dto = bookingMapper.mapBooking(booking);

        assertNotNull(dto);
        assertEquals(booking.getId(), dto.getId());
        assertEquals(booking.getCheckInDate(), dto.getCheckInDate());
        assertEquals(booking.getCheckOutDate(), dto.getCheckOutDate());
        assertEquals(booking.getCreatedAt(), dto.getCreatedAt());
        assertEquals(booking.getIsPaid(), dto.getIsPaid());
        assertEquals(booking.getClient(), dto.getClient());

        UnitDTO unitDTO = dto.getUnit();
        assertNotNull(unitDTO);
        assertEquals(unit.getRooms(), unitDTO.getRooms());
        assertEquals(unit.getFloor(), unitDTO.getFloor());
        assertEquals(unit.getType(), unitDTO.getType());
    }

    @Test
    void mapUnit_ShouldMapUnitFieldsCorrectly() {
        Unit unit = Unit.builder()
                .id(5L)
                .price(BigDecimal.valueOf(85))
                .rooms(1)
                .floor(1)
                .type(AccommodationType.FLAT)
                .build();

        UnitDTO unitDTO = bookingMapper.mapUnit(unit);

        assertNotNull(unitDTO);
        assertEquals(unit.getRooms(), unitDTO.getRooms());
        assertEquals(unit.getFloor(), unitDTO.getFloor());
        assertEquals(unit.getType(), unitDTO.getType());
    }
}
