package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest, List<ShortItemDto> items) {
        log.info("ItemRequest в маппер: {}", itemRequest);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
        log.info("ItemRequestDto из маппера: {}", itemRequestDto);
        return itemRequestDto;
    }

    public static ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        log.info("ShortItemRequest в маппер: {}", itemRequest);
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
        log.info("ShortItemRequestDto из маппера: {}", itemRequestDto);
        return itemRequestDto;
    }

    public static ItemRequest mapToItemRequest(RequestDto requestDto, User requester) {
        log.info("RequestDto {} и User {} в маппер", requestDto, requester);
        ItemRequest itemRequest = ItemRequest.builder()
                .description(requestDto.getDescription())
                .created(LocalDateTime.now())
                .requester(requester)
                .build();
        log.info("ItemRequest из маппера: {}", itemRequest);
        return itemRequest;
    }
}
