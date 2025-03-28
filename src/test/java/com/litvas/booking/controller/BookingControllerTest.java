package com.litvas.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.litvas.booking.dto.BookReservationDTO;
import com.litvas.booking.dto.BookingDTO;
import com.litvas.booking.dto.BookingModificateDTO;
import com.litvas.booking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDTO bookingDTO;
    private BookingModificateDTO bookingModificateDTO;
    private BookReservationDTO bookReservationDTO;

    @BeforeEach
    void setUp() {
        bookingDTO = new BookingDTO();
        bookingDTO.setUnitId(1L);
        bookingDTO.setCheckInDate(LocalDate.now());
        bookingDTO.setCheckOutDate(LocalDate.now().plusDays(3));

        bookingModificateDTO = new BookingModificateDTO();
        bookingModificateDTO.setBookingId(10L);

        bookReservationDTO = new BookReservationDTO();
    }

    @Test
    void bookUnits_ShouldReturnOk() throws Exception {
        Mockito.when(bookingService.bookUnit(any(), any(), any())).thenReturn(bookReservationDTO);

        mockMvc.perform(post("/api/booking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void cancelBooking_ShouldReturnOk() throws Exception {
        Mockito.when(bookingService.cancelBooking(any())).thenReturn(bookReservationDTO);

        mockMvc.perform(put("/api/booking/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingModificateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void paidBooking_ShouldReturnOk() throws Exception {
        Mockito.when(bookingService.paidBooking(any())).thenReturn(bookReservationDTO);

        mockMvc.perform(put("/api/booking/paid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingModificateDTO)))
                .andExpect(status().isOk());
    }
}