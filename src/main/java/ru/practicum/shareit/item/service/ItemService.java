package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long ownerId, ItemDto item);

    ItemDto getItem(Long ownerId, Long itemId);

    List<ItemDto> getItems(Long ownerId);

    List<ItemDto> getItemsByText(Long userId, String text);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto item);
}
