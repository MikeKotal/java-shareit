package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(Long ownerId, Item item) {
        log.info("Запрос от пользователя {} на создание вещи {}", ownerId, item);
        checkUserExists(ownerId);
        item.setOwner(ownerId);
        item = itemRepository.addItem(item);
        log.info("Вещь успешно создана {}", item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    public ItemDto getItem(Long ownerId, Long itemId) {
        log.info("Запрос на получение вещи с itemId {}", itemId);
        checkUserExists(ownerId);
        ItemDto itemDto = ItemMapper.mapToItemDto(getItemById(itemId));
        log.info("Вещь с itemId '{} = '{}'", itemId, itemDto);
        return itemDto;
    }

    @Override
    public List<ItemDto> getItems(Long ownerId) {
        checkUserExists(ownerId);
        List<ItemDto> items = itemRepository.findItems(ownerId)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
        log.info("Все вещи {}", items);
        return items;
    }

    @Override
    public List<ItemDto> getItemsByText(Long userId, String text) {
        checkUserExists(userId);
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        List<ItemDto> items = itemRepository.findItemsByText(text.toLowerCase())
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
        log.info("Все отфильтрованные вещи {} по тексту {}", items, text);
        return items;
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, Item item) {
        log.info("Запрос на обновление вещи с itemId {}", itemId);
        checkUserExists(ownerId);
        Item oldItem = getItemById(itemId);
        if (!Objects.equals(oldItem.getOwner(), ownerId)) {
            throw new ValidationException("Вещь допустимо менять только собственнику");
        }
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        oldItem = itemRepository.updateItem(oldItem);
        log.info("Вещь {} успешно обновлена", oldItem);
        return ItemMapper.mapToItemDto(oldItem);
    }

    private void checkUserExists(Long ownerId) {
        if (!userRepository.checkUserExists(ownerId)) {
            throw new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено", ownerId));
        }
    }

    private Item getItemById(Long id) {
        return itemRepository.findItem(id)
                .orElseThrow(() -> {
                    log.error("Вещь с id {} отсутствует", id);
                    return new NotFoundException(String.format("Вещи с идентификатором = '%s' не найдено", id));
                });
    }
}
