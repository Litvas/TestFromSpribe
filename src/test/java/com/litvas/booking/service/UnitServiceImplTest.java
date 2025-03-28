package com.litvas.booking.service;

import com.litvas.booking.domain.AccommodationType;
import com.litvas.booking.domain.Unit;
import com.litvas.booking.repository.UnitRepository;
import com.litvas.booking.service.impl.UnitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitServiceImplTest {

    @Mock
    private UnitRepository unitRepository;

    @InjectMocks
    private UnitServiceImpl unitService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void searchAvailableUnits_ShouldReturnUnits() {
        Unit unit = Unit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .build();

        Page<Unit> page = new PageImpl<>(List.of(unit));
        when(unitRepository.findAvailableUnits(any(), any(), any(), any())).thenReturn(page);

        Page<Unit> result = unitService.searchAvailableUnits(
                BigDecimal.valueOf(100),
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                pageable
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(unitRepository, times(1)).findAvailableUnits(any(), any(), any(), any());
    }

    @Test
    void searchAvailableUnits_ShouldReturnEmptyPage() {
        Page<Unit> emptyPage = new PageImpl<>(Collections.emptyList());
        when(unitRepository.findAvailableUnits(any(), any(), any(), any())).thenReturn(emptyPage);

        Page<Unit> result = unitService.searchAvailableUnits(
                BigDecimal.valueOf(200),
                LocalDate.now(),
                LocalDate.now().plusDays(1),
                pageable
        );

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void generateAccommodationType_ShouldReturnValidType() throws Exception {
        Method method = UnitServiceImpl.class.getDeclaredMethod("generateAccommodationType");
        method.setAccessible(true);

        for (int i = 0; i < 100; i++) {
            Object result = method.invoke(unitService);
            assertNotNull(result);
            assertTrue(result instanceof AccommodationType);
        }
    }
}
