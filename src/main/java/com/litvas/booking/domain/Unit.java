package com.litvas.booking.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "units")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int rooms;

    @Enumerated(EnumType.STRING)
    private AccommodationType type;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Booking> bookings;
}
