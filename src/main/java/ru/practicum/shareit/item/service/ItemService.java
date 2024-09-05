package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto createItem(Long ownerId, Item item);

    ItemDto getItem(Long ownerId, Long itemId);

    List<ItemDto> getItems(Long ownerId);

    List<ItemDto> getItemsByText(Long userId, String text);

    ItemDto updateItem(Long ownerId, Long itemId, Item item);
}
