package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long ownerId, ItemRequestDto item);

    ItemBookingDto getItem(Long ownerId, Long itemId);

    List<ItemBookingDto> getItems(Long ownerId);

    List<ItemDto> getItemsByText(Long userId, String text);

    ItemDto updateItem(Long ownerId, Long itemId, ItemRequestDto item);

    CommentDto createComment(Long bookerId, Long itemId, CommentRequestDto commentDto);
}
