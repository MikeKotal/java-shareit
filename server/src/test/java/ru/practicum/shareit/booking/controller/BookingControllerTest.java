package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestData.BOOKING_DTO;
import static ru.practicum.shareit.TestData.BOOKING_REQUEST_DTO;
import static ru.practicum.shareit.TestData.COMMENT_DTO;
import static ru.practicum.shareit.TestData.ITEM_DTO;
import static ru.practicum.shareit.TestData.USER_DTO;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void whenCallSaveBookingThenReturnNewBooking() throws Exception {
        when(bookingService.createBooking(1L, BOOKING_REQUEST_DTO))
                .thenReturn(BOOKING_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(post("/bookings")
                        .headers(headers)
                        .content(mapper.writeValueAsString(BOOKING_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(BOOKING_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(BOOKING_DTO.getItemId()), Long.class))
                .andExpect(jsonPath("$.start", is(BOOKING_DTO.getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$.end", is(BOOKING_DTO.getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$.status", is(BOOKING_DTO.getStatus().name()), String.class));
    }

    @Test
    public void whenCallApproveBookingThenReturnApprovedBooking() throws Exception {
        when(bookingService.approveBooking(1L, 1L, Boolean.FALSE))
                .thenReturn(BOOKING_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(patch(String.format("/bookings/%s?approved=%s", "1", "false"))
                        .headers(headers)
                        .content(mapper.writeValueAsString(BOOKING_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(BOOKING_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(BOOKING_DTO.getItemId()), Long.class))
                .andExpect(jsonPath("$.start", is(BOOKING_DTO.getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$.end", is(BOOKING_DTO.getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$.status", is(BOOKING_DTO.getStatus().name()), String.class));
    }

    @Test
    public void whenCallGetBookingThenReturnBooking() throws Exception {
        when(bookingService.getBooking(1L, 1L))
                .thenReturn(BOOKING_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/bookings/%s", "1"))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(BOOKING_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(BOOKING_DTO.getItemId()), Long.class))
                .andExpect(jsonPath("$.start", is(BOOKING_DTO.getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$.end", is(BOOKING_DTO.getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$.status", is(BOOKING_DTO.getStatus().name()), String.class))
                .andExpect(jsonPath("$.item.id", is(ITEM_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(ITEM_DTO.getName()), String.class))
                .andExpect(jsonPath("$.item.description", is(ITEM_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$.item.available", is(ITEM_DTO.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.item.comments[0]", notNullValue()))
                .andExpect(jsonPath("$.item.comments[0].id", is(COMMENT_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.item.comments[0].text", is(COMMENT_DTO.getText()), String.class))
                .andExpect(jsonPath("$.item.comments[0].authorName", is(COMMENT_DTO.getAuthorName()), String.class))
                .andExpect(jsonPath("$.item.comments[0].created", is(COMMENT_DTO.getCreated()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$.booker.id", is(USER_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(USER_DTO.getName()), String.class))
                .andExpect(jsonPath("$.booker.email", is(USER_DTO.getEmail()), String.class));
    }

    @Test
    public void whenCallGetBookingsByBookerIdThenReturnListSize1() throws Exception {
        when(bookingService.getBookingsByBookerId(1L, Status.WAITING))
                .thenReturn(List.of(BOOKING_DTO));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/bookings?state=%s", Status.WAITING))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(BOOKING_DTO.getId()), Long.class))
                .andExpect(jsonPath("$[0].itemId", is(BOOKING_DTO.getItemId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(BOOKING_DTO.getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$[0].end", is(BOOKING_DTO.getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$[0].status", is(BOOKING_DTO.getStatus().name()), String.class))
                .andExpect(jsonPath("$[0].item", notNullValue()))
                .andExpect(jsonPath("$[0].item.comments", notNullValue()))
                .andExpect(jsonPath("$[0].booker", notNullValue()));
    }

    @Test
    public void whenCallGetBookingsByOwnerIdThenReturnListSize1() throws Exception {
        when(bookingService.getBookingsByOwnerId(1L, Status.ALL))
                .thenReturn(List.of(BOOKING_DTO));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/bookings/owner?state=%s", Status.ALL))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(BOOKING_DTO.getId()), Long.class))
                .andExpect(jsonPath("$[0].itemId", is(BOOKING_DTO.getItemId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(BOOKING_DTO.getStart()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$[0].end", is(BOOKING_DTO.getEnd()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class))
                .andExpect(jsonPath("$[0].status", is(BOOKING_DTO.getStatus().name()), String.class))
                .andExpect(jsonPath("$[0].item", notNullValue()))
                .andExpect(jsonPath("$[0].item.comments", notNullValue()))
                .andExpect(jsonPath("$[0].booker", notNullValue()));
    }

    @Test
    public void whenSendInvalidRequestThenReturnBadRequest400() throws Exception {
        when(bookingService.createBooking(1L, BOOKING_REQUEST_DTO))
                .thenThrow(new ValidationException("Тест"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(post("/bookings")
                        .headers(headers)
                        .content(mapper.writeValueAsString(BOOKING_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Тест"), String.class));
    }

    @Test
    public void whenSendRequestWithUnknownUserThenReturnNotFound404() throws Exception {
        when(bookingService.getBooking(1L, 1L))
                .thenThrow(new NotFoundException("Тест"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/bookings/%s", "1"))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Тест"), String.class));
    }

    @Test
    public void checkInternalServerErrorException500() throws Exception {
        when(bookingService.getBooking(1L, 1L))
                .thenThrow(new RuntimeException("Тест"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/bookings/%s", "1"))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error", is("Произошла непредвиденная ошибка."), String.class));
    }
}
