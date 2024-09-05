package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {

    Item addItem(Item item);

    Optional<Item> findItem(Long id);

    Collection<Item> findItems(Long ownerId);

    Collection<Item> findItemsByText(String text);

    Item updateItem(Item item);
}
