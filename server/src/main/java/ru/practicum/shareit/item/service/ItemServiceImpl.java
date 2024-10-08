package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemReqDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static ru.practicum.shareit.booking.service.BookingServiceImpl.ORDER_BY_START_DATE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    @Transactional
    public ItemDto createItem(Long ownerId, ItemReqDto item) {
        log.info("Запрос от пользователя {} на создание вещи {}", ownerId, item);
        User user = getUserById(ownerId);
        ItemRequest itemRequest = null;
        if (item.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(item.getRequestId()).orElseThrow(() -> {
                log.error("Запрос с id {} отсутствует", item.getRequestId());
                return new NotFoundException(String.format("Запроса с идентификатором = '%s' не найдено",
                        item.getRequestId()));
            });
        }
        Item newItem = itemRepository.save(ItemMapper.mapToItem(item, user, itemRequest));
        log.info("Вещь успешно создана {}", newItem);
        return ItemMapper.mapToItemDto(newItem);
    }

    @Override
    public ItemBookingDto getItem(Long ownerId, Long itemId) {
        log.info("Запрос на получение вещи с itemId {}", itemId);
        Item item = getItemById(itemId);
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        LocalDateTime current = LocalDateTime.now();
        ItemBookingDto itemDto = ItemMapper.mapToItemBookingDto(item,
                bookingRepository.findFirstByItemIdAndItemOwnerIdAndEndDateBefore(item.getId(),
                        ownerId, current, ORDER_BY_START_DATE),
                bookingRepository.findFirstByItemIdAndItemOwnerIdAndStartDateAfter(item.getId(),
                        ownerId, current, ORDER_BY_START_DATE),
                CommentMapper.mapToCommentDto(comments));
        log.info("Вещь с itemId '{} = '{}'", itemId, itemDto);
        return itemDto;
    }

    @Override
    public List<ItemBookingDto> getItems(Long ownerId) {
        checkUserExists(ownerId);
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        log.info("Все вещи {}", items);
        LocalDateTime current = LocalDateTime.now();
        return items.stream()
                .map(item -> ItemMapper.mapToItemBookingDto(item,
                        bookingRepository.findFirstByItemIdAndItemOwnerIdAndEndDateBefore(item.getId(),
                                item.getOwner().getId(), current, ORDER_BY_START_DATE),
                        bookingRepository.findFirstByItemIdAndItemOwnerIdAndStartDateAfter(item.getId(),
                                item.getOwner().getId(), current, ORDER_BY_START_DATE),
                        CommentMapper.mapToCommentDto(commentRepository.findAllByItemId(item.getId()))))
                .toList();
    }

    @Override
    public List<ItemDto> getItemsByText(Long userId, String text) {
        checkUserExists(userId);
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        List<Item> items = itemRepository.findItemsByText(text);
        log.info("Все отфильтрованные вещи {} по тексту {}", items, text);
        return ItemMapper.mapToItemDto(items);
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long ownerId, Long itemId, ItemReqDto item) {
        log.info("Запрос на обновление вещи с itemId {}", itemId);
        checkUserExists(ownerId);
        Item oldItem = getItemById(itemId);
        if (!Objects.equals(oldItem.getOwner().getId(), ownerId)) {
            throw new ValidationException("Вещь допустимо менять только собственнику");
        }
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setIsAvailable(item.getAvailable());
        }
        oldItem = itemRepository.save(oldItem);
        log.info("Вещь {} успешно обновлена", oldItem);
        return ItemMapper.mapToItemDto(oldItem);
    }

    @Override
    @Transactional
    public CommentDto createComment(Long bookerId, Long itemId, CommentRequestDto commentDto) {
        log.info("Запрос на добавление комментария {} от пользователя {}", commentDto, bookerId);
        Booking booking = bookingRepository
                .findFirstByBookerIdAndItemIdAndEndDateBefore(bookerId, itemId,
                        LocalDateTime.now())
                .orElseThrow(() -> new ValidationException("У пользователя не было бронирований " +
                        "или текущее бронирование не началось/закончилось"));
        Comment comment = commentRepository.save(CommentMapper.mapToComment(commentDto, booking.getItem(),
                booking.getBooker()));
        log.info("Комментарий {} успешно создан", comment);
        return CommentMapper.mapToCommentDto(comment);
    }

    private void checkUserExists(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено", ownerId));
        }
    }

    private Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Вещь с id {} отсутствует", id);
                    return new NotFoundException(String.format("Вещи с идентификатором = '%s' не найдено", id));
                });
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} отсутствует", id);
                    return new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено", id));
                });
    }
}
