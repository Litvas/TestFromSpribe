package com.litvas.booking.service.impl;

import com.litvas.booking.domain.AccommodationType;
import com.litvas.booking.domain.Unit;
import com.litvas.booking.repository.UnitRepository;
import com.litvas.booking.service.UnitService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2
public class UnitServiceImpl implements UnitService {

    private final Random random = new Random();

    private final UnitRepository unitRepository;

    @Override
    @CacheEvict(value = "availableUnits", allEntries = true)
    public Page<Unit> searchAvailableUnits(BigDecimal price, LocalDate checkInDate, LocalDate checkOutDate, Pageable pageable) {
        return unitRepository.findAvailableUnits(price, checkInDate, checkOutDate, pageable);
    }

    @PostConstruct
    private void generateUnits() {
        int testedUnits = 90;
        List<Unit> units = new ArrayList<>(testedUnits);
        for (int i = 0; i < testedUnits; i++) {
            Unit unit = Unit.builder()
                    .rooms(random.nextInt(5) + 1)
                    .type(generateAccommodationType())
                    .floor(random.nextInt(20) + 1)
                    .price(BigDecimal.valueOf(50 + random.nextInt(251)))
                    .build();
            units.add(unit);
        }
        unitRepository.saveAll(units);
        log.info("Generated {} tested entries", testedUnits);
    }

    private AccommodationType generateAccommodationType() {
        AccommodationType[] accommodationTypes = AccommodationType.values();
        return accommodationTypes[random.nextInt(accommodationTypes.length)];
    }
}
