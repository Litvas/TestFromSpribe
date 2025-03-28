package com.litvas.booking.mapper;

import com.litvas.booking.domain.Booking;
import com.litvas.booking.domain.Unit;
import com.litvas.booking.dto.BookReservationDTO;
import com.litvas.booking.dto.UnitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target="id", source = "booking.id")
    @Mapping(target="checkInDate", source = "booking.checkInDate")
    @Mapping(target="checkOutDate", source = "booking.checkOutDate")
    @Mapping(target="createdAt", source = "booking.createdAt")
    @Mapping(target="isPaid", source = "booking.isPaid")
    @Mapping(target="client", source = "booking.client")
    @Mapping(target="unit", expression = "java(mapUnit(booking.getUnit()))")
    BookReservationDTO mapBooking(Booking booking);

    @Mapping(target="rooms", source = "unit.rooms")
    @Mapping(target="type", source = "unit.type")
    @Mapping(target="floor", source = "unit.floor")
    UnitDTO mapUnit(Unit unit);

}
