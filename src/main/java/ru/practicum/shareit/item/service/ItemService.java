package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long ownerId, ItemDto item);

    ItemBookingDto getItem(Long ownerId, Long itemId);

    List<ItemBookingDto> getItems(Long ownerId);

    List<ItemDto> getItemsByText(Long userId, String text);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto item);

    CommentDto createComment(Long bookerId, Long itemId, CommentDto commentDto);
}
