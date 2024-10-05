package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.service.ItemService;

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
import static ru.practicum.shareit.TestData.COMMENT_DTO;
import static ru.practicum.shareit.TestData.COMMENT_REQUEST_DTO;
import static ru.practicum.shareit.TestData.ITEM_BOOKING_DTO;
import static ru.practicum.shareit.TestData.ITEM_DTO;
import static ru.practicum.shareit.TestData.ITEM_REQ_DTO;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void whenCallCreateItemThenReturnNewItem() throws Exception {
        when(itemService.createItem(1L, ITEM_REQ_DTO))
                .thenReturn(ITEM_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(post("/items")
                        .headers(headers)
                        .content(mapper.writeValueAsString(ITEM_REQ_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(ITEM_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(ITEM_DTO.getName()), String.class))
                .andExpect(jsonPath("$.description", is(ITEM_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(ITEM_DTO.getAvailable()), Boolean.class));
    }

    @Test
    public void whenCallGetItemThenReturnItem() throws Exception {
        when(itemService.getItem(1L, 1L))
                .thenReturn(ITEM_BOOKING_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/items/%s", "1"))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ITEM_BOOKING_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(ITEM_BOOKING_DTO.getName()), String.class))
                .andExpect(jsonPath("$.description", is(ITEM_BOOKING_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(ITEM_BOOKING_DTO.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.lastBooking", is(ITEM_BOOKING_DTO.getLastBooking()), String.class))
                .andExpect(jsonPath("$.nextBooking", is(ITEM_BOOKING_DTO.getNextBooking()), String.class))
                .andExpect(jsonPath("$.comments", notNullValue()))
                .andExpect(jsonPath("$.comments[0].id", is(COMMENT_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].text", is(COMMENT_DTO.getText()), String.class))
                .andExpect(jsonPath("$.comments[0].authorName", is(COMMENT_DTO.getAuthorName()), String.class))
                .andExpect(jsonPath("$.comments[0].created", is(COMMENT_DTO.getCreated()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class));
    }

    @Test
    public void whenCallGetItemsThenReturnListSize1() throws Exception {
        when(itemService.getItems(1L))
                .thenReturn(List.of(ITEM_BOOKING_DTO));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get("/items")
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ITEM_BOOKING_DTO.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(ITEM_BOOKING_DTO.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(ITEM_BOOKING_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(ITEM_BOOKING_DTO.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].lastBooking", is(ITEM_BOOKING_DTO.getLastBooking()), String.class))
                .andExpect(jsonPath("$[0].nextBooking", is(ITEM_BOOKING_DTO.getNextBooking()), String.class))
                .andExpect(jsonPath("$[0].comments", notNullValue()));
    }

    @Test
    public void whenCallGetItemsByTextThenReturnListSize1() throws Exception {
        when(itemService.getItemsByText(1L, "Text"))
                .thenReturn(List.of(ITEM_DTO));

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(get(String.format("/items/search?text=%s", "Text"))
                        .headers(headers)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(ITEM_DTO.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(ITEM_DTO.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(ITEM_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available", is(ITEM_DTO.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].comments", notNullValue()));
    }

    @Test
    public void whenCallUpdateItemThenItemReturnUpdated() throws Exception {
        when(itemService.updateItem(1L, 1L, ITEM_REQ_DTO))
                .thenReturn(ITEM_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(patch(String.format("/items/%s", 1L))
                        .headers(headers)
                        .content(mapper.writeValueAsString(ITEM_REQ_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ITEM_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(ITEM_DTO.getName()), String.class))
                .andExpect(jsonPath("$.description", is(ITEM_DTO.getDescription()), String.class))
                .andExpect(jsonPath("$.available", is(ITEM_DTO.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.comments", notNullValue()));
    }

    @Test
    public void whenCallCreateCommentThenReturnNewComment() throws Exception {
        when(itemService.createComment(1L, 1L, COMMENT_REQUEST_DTO))
                .thenReturn(COMMENT_DTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");

        mockMvc.perform(post(String.format("/items/%s/comment", "1"))
                        .headers(headers)
                        .content(mapper.writeValueAsString(COMMENT_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(COMMENT_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(COMMENT_DTO.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(COMMENT_DTO.getAuthorName()), String.class))
                .andExpect(jsonPath("$.created", is(COMMENT_DTO.getCreated()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), String.class));
    }
}
