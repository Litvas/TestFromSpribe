package com.litvas.booking.controller;

import com.litvas.booking.domain.Unit;
import com.litvas.booking.service.UnitService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UnitController.class)
class UnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UnitService unitService;

    @Test
    void searchAvailableUnits_ShouldReturnOneUnit() throws Exception {
        Unit unit = Unit.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .build();
        Page<Unit> page = new PageImpl<>(List.of(unit));

        Mockito.when(unitService.searchAvailableUnits(any(), any(), any(), any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/units")
                        .param("price", "100")
                        .param("checkInDate", LocalDate.now().toString())
                        .param("checkOutDate", LocalDate.now().plusDays(2).toString())
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .accept("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    void searchAvailableUnits_ShouldReturnEmptyList() throws Exception {
        Page<Unit> emptyPage = new PageImpl<>(Collections.emptyList());

        Mockito.when(unitService.searchAvailableUnits(any(), any(), any(), any()))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/units")
                        .param("price", "200")
                        .param("checkInDate", LocalDate.now().toString())
                        .param("checkOutDate", LocalDate.now().plusDays(1).toString())
                        .param("pageNumber", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));
    }
}
