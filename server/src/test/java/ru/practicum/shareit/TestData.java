package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemReqDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;

import java.time.LocalDateTime;
import java.util.List;

public class TestData {

    public static final UserDto USER_DTO = UserDto.builder()
            .id(1L)
            .name("Пользователь")
            .email("email@email.com")
            .build();

    public static final CommentDto COMMENT_DTO = CommentDto.builder()
            .id(1L)
            .text("Комментарий")
            .authorName("Пользователь")
            .created(LocalDateTime.parse("2024-10-02T00:00:00"))
            .build();

    public static final ItemDto ITEM_DTO = ItemDto.builder()
            .id(1L)
            .name("Тест")
            .description("Описание")
            .available(Boolean.TRUE)
            .comments(List.of(COMMENT_DTO))
            .build();

    public static final BookingDto BOOKING_DTO = BookingDto.builder()
            .id(1L)
            .itemId(1L)
            .start(LocalDateTime.parse("2024-10-01T00:00:00"))
            .end(LocalDateTime.parse("2024-10-02T00:00:00"))
            .status(Status.WAITING)
            .item(ITEM_DTO)
            .booker(USER_DTO)
            .build();

    public static final ItemBookingDto ITEM_BOOKING_DTO = ItemBookingDto.builder()
            .id(1L)
            .name("Тест")
            .description("Описание")
            .available(Boolean.TRUE)
            .lastBooking("2024-09-03T00:00 - 2024-09-04T00:00")
            .nextBooking("2024-09-05T00:00 - 2024-09-06T00:00")
            .comments(List.of(COMMENT_DTO))
            .build();

    public static final ItemRequestDto ITEM_REQUEST_DTO = ItemRequestDto.builder()
            .id(1L)
            .description("Описание")
            .created(LocalDateTime.parse("2024-10-01T00:00:00"))
            .build();

    public static final BookingRequestDto BOOKING_REQUEST_DTO = BookingRequestDto.builder()
            .itemId(1L)
            .start(LocalDateTime.parse("2024-10-01T00:00:00"))
            .end(LocalDateTime.parse("2024-10-02T00:00:00"))
            .build();

    public static final ItemReqDto ITEM_REQ_DTO = ItemReqDto.builder()
            .name("Вещь")
            .description("Описание вещи")
            .available(Boolean.TRUE)
            .requestId(1L)
            .build();

    public static final CommentRequestDto COMMENT_REQUEST_DTO = CommentRequestDto.builder()
            .text("Test")
            .build();

    public static final RequestDto REQUEST_DTO = RequestDto.builder()
            .description("Описание")
            .build();

    public static final UserRequestDto USER_REQUEST_DTO = UserRequestDto.builder()
            .name("Имя")
            .email("email.email.ru")
            .build();
}
