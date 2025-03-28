package com.litvas.booking.dto;

import com.litvas.booking.domain.Client;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookReservationDTO {

    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private Boolean isPaid;
    private Client client;
    private BigDecimal price;
    private UnitDTO unit;

}
