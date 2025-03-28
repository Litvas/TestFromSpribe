package com.litvas.booking.dto;

import com.litvas.booking.domain.AccommodationType;
import lombok.Data;

@Data
public class UnitDTO {

    private int rooms;
    private AccommodationType type;
    private Integer floor;

}
