package ru.practicum.shareit.item.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReqDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImplTest {

    private final EntityManager em;
    private final ItemService itemService;

    @Test
    public void checkSuccessCreateItem() {
        ItemReqDto itemReqDto = prepareItem("Тест", "Тестовая");
        ItemDto created = itemService.createItem(2L, itemReqDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", created.getId()).getSingleResult();

        assertThat(item.getId(), equalTo(created.getId()));
        assertThat(item.getName(), equalTo(itemReqDto.getName()));
        assertThat(item.getDescription(), equalTo(itemReqDto.getDescription()));
        assertThat(item.getIsAvailable(), is(Boolean.TRUE));
        assertThat(item.getOwner(), notNullValue());
    }

    @Test
    public void checkSuccessGetItem() {
        ItemBookingDto itemDto = itemService.getItem(1L, 1L);

        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo("Дрель"));
        assertThat(itemDto.getDescription(), equalTo("Для сверления стен"));
        assertThat(itemDto.getAvailable(), is(Boolean.TRUE));
        assertThat(itemDto.getLastBooking(), equalTo("2024-09-03T00:00 - 2024-09-04T00:00"));
        assertThat(itemDto.getNextBooking(), nullValue());
    }

    @Test
    public void checkSuccessGetItems() {
        List<ItemBookingDto> bookingDtos = itemService.getItems(1L);

        assertThat(bookingDtos.size(), equalTo(2));
    }

    @Test
    public void checkSuccessGetItemsByText() {
        List<ItemDto> itemDtos = itemService.getItemsByText(2L, "для");

        assertThat(itemDtos.size(), equalTo(2));
    }

    @Test
    public void checkSuccessItemUpdate() {
        ItemReqDto itemReqDto = prepareItem("Обновление", "Серьезное");
        itemService.updateItem(1L, 1L, itemReqDto);

        TypedQuery<Item> query = em.createQuery("Select i from Item i where i.id = :id", Item.class);
        Item item = query.setParameter("id", 1L).getSingleResult();

        assertThat(item.getId(), equalTo(1L));
        assertThat(item.getName(), equalTo(itemReqDto.getName()));
        assertThat(item.getDescription(), equalTo(itemReqDto.getDescription()));
        assertThat(item.getIsAvailable(), is(Boolean.TRUE));
        assertThat(item.getOwner(), notNullValue());
    }

    @Test
    public void checkSuccessCreateComment() {
        itemService.createComment(3L, 1L, CommentRequestDto.builder().text("Тест").build());

        TypedQuery<Comment> query = em.createQuery("Select c from Comment c where c.id = :id", Comment.class);
        Comment comment = query.setParameter("id", 2L).getSingleResult();

        assertThat(comment.getId(), equalTo(2L));
        assertThat(comment.getText(), equalTo("Тест"));
        assertThat(comment.getItem(), notNullValue());
        assertThat(comment.getAuthor(), notNullValue());
        assertThat(comment.getCreated(), notNullValue());
    }

    private ItemReqDto prepareItem(String name, String description) {
        return ItemReqDto.builder()
                .name(name)
                .description(description)
                .available(Boolean.TRUE)
                .build();
    }
}
