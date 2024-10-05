package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(Long requesterId, RequestDto requestDto);

    List<ItemRequestDto> getItemRequestsByRequesterId(Long requesterId);

    List<ItemRequestDto> getItemRequests(Long userId, Integer page);

    ItemRequestDto getItemRequest(Long userId, Long requestId);
}
