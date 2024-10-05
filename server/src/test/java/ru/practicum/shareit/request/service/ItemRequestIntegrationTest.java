package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestIntegrationTest {

    private final EntityManager em;
    private final ItemRequestService itemRequestService;

    @Test
    public void checkSuccessCreateItemRequest() {
        itemRequestService.createItemRequest(1L, RequestDto.builder().description("Тест").build());

        TypedQuery<ItemRequest> query = em.createQuery("Select r from ItemRequest r where r.id = :id", ItemRequest.class);
        ItemRequest itemRequest = query.setParameter("id", 2L).getSingleResult();

        assertThat(itemRequest.getId(), equalTo(2L));
        assertThat(itemRequest.getDescription(), equalTo("Тест"));
        assertThat(itemRequest.getCreated(), notNullValue());
        assertThat(itemRequest.getRequester(), notNullValue());
    }

    @Test
    public void checkSuccessGetItemRequestByRequesterId() {
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getItemRequestsByRequesterId(3L);

        assertThat(itemRequestDtos.size(), equalTo(1));
    }

    @Test
    public void checkSuccessGetItemRequests() {
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getItemRequests(1L, 0);

        assertThat(itemRequestDtos.size(), equalTo(1));
    }

    @Test
    public void checkSuccessGetItemRequest() {
        ItemRequestDto itemRequestDto = itemRequestService.getItemRequest(3L, 1L);

        assertThat(itemRequestDto.getId(), equalTo(1L));
        assertThat(itemRequestDto.getDescription(), equalTo("Нужна болгарка"));
        assertThat(itemRequestDto.getCreated(), equalTo(LocalDateTime.parse("2024-09-04T00:00")));
    }
}
