package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        item.setId(getNextId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findItem(Long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<Item> findItems(Long ownerId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .toList();
    }

    @Override
    public Collection<Item> findItemsByText(String text) {
        return items.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text))
                .toList();
    }

    @Override
    public Item updateItem(Item item) {
        Item updatedItem = items.get(item.getId());
        updatedItem.setName(item.getName());
        updatedItem.setDescription(item.getDescription());
        updatedItem.setAvailable(item.getAvailable());
        return updatedItem;
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
