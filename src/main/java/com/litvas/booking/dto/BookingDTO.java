package com.litvas.booking.dto;

import com.litvas.booking.domain.Client;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class BookingDTO {

    @NotBlank
    private Long unitId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

}
