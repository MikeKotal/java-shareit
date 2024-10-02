package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    public static final Sort ORDER_BY_CREATED_AT = Sort.by(Sort.Direction.DESC, "created");
    public static final Integer PAGE_SIZE = 10;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(Long requesterId, RequestDto requestDto) {
        log.info("Запрос от пользователя {} на создание запроса {}", requesterId, requestDto);
        User user = userRepository.findById(requesterId)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} отсутствует", requesterId);
                    return new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено",
                            requesterId));
                });
        ItemRequest itemRequest = itemRequestRepository.save(ItemRequestMapper.mapToItemRequest(requestDto, user));
        log.info("Запрос успешно создан {}", itemRequest);
        return ItemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequesterId(Long requesterId) {
        log.info("Запрос на получение списка запросов пользователя с requesterId {}", requesterId);
        checkUserExists(requesterId);
        List<ItemRequestDto> requestDtos = itemRequestRepository.findAllByRequesterId(requesterId, ORDER_BY_CREATED_AT)
                .stream()
                .map(itemRequest -> ItemRequestMapper.mapToItemRequestDto(itemRequest,
                        itemRepository.findAllByRequestId(itemRequest.getId())))
                .toList();
        log.info("Список запросов {} от пользователя {}", requestDtos, requesterId);
        return requestDtos;
    }

    @Override
    public List<ItemRequestDto> getItemRequests(Long userId, Integer page) {
        log.info("Запрос на получение списка запросов от пользователя {}", userId);
        checkUserExists(userId);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, ORDER_BY_CREATED_AT);
        Page<ItemRequest> requestDtos = itemRequestRepository.findAll(pageable);
        return requestDtos.getContent().stream()
                .map(itemRequest -> ItemRequestMapper.mapToItemRequestDto(itemRequest,
                        itemRepository.findAllByRequestId(itemRequest.getId())))
                .toList();
    }

    @Override
    public ItemRequestDto getItemRequest(Long userId, Long requestId) {
        log.info("Запрос на получение запроса {} от пользователя {}", requestId, userId);
        checkUserExists(userId);
        ItemRequestDto itemRequestDto = ItemRequestMapper.mapToItemRequestDto(itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    log.error("Запрос с id {} отсутствует", requestId);
                    return new NotFoundException(String.format("Запроса с идентификатором = '%s' не найдено", requestId));
                }), itemRepository.findAllByRequestId(requestId));
        log.info("Запрос с requestId '{} = '{}'", requestId, itemRequestDto);
        return itemRequestDto;
    }

    private void checkUserExists(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено", ownerId));
        }
    }
}
