package ru.practicum.shareit.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestData.ITEM_REQUEST_DTO;
import static ru.practicum.shareit.TestData.REQUEST_DTO;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void whenCallCreateItemRequestThenReturnNewRequest() throws Exception {
        when(itemRequestService.createItemRequest(1L, REQUEST_DTO))
                .thenReturn(ITEM_REQUEST_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(post("/requests")
                        .headers(headers)
                        .content(mapper.writeValueAsString(REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(ITEM_REQUEST_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(ITEM_REQUEST_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$.created", is(ITEM_REQUEST_DTO.getCreated()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class));
    }

    @Test
    public void whenCallGetRequestsByRequesterIdThenReturnListSize1() throws Exception {
        when(itemRequestService.getItemRequestsByRequesterId(1L))
                .thenReturn(List.of(ITEM_REQUEST_DTO));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get("/requests")
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ITEM_REQUEST_DTO.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(ITEM_REQUEST_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$[0].created", is(ITEM_REQUEST_DTO.getCreated()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class));
    }

    @Test
    public void whenCallGetItemRequestsThenReturnListSize1() throws Exception {
        when(itemRequestService.getItemRequests(1L, 0))
                .thenReturn(List.of(ITEM_REQUEST_DTO));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/requests/all?page=%s", "0"))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ITEM_REQUEST_DTO.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(ITEM_REQUEST_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$[0].created", is(ITEM_REQUEST_DTO.getCreated()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class));
    }

    @Test
    public void whenCallGetItemRequestByIdThenReturnItemRequest() throws Exception {
        when(itemRequestService.getItemRequest(1L, 1L))
                .thenReturn(ITEM_REQUEST_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/requests/%s", "1"))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ITEM_REQUEST_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(ITEM_REQUEST_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$.created", is(ITEM_REQUEST_DTO.getCreated()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class));
    }
}
