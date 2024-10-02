package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto mapToItemDto(Item item) {
        log.info("Item в маппер: {}", item);
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .build();
        log.info("ItemDto из маппера: {}", itemDto);
        return itemDto;
    }

    public static Item mapToItem(ItemRequestDto itemDto, User user, ItemRequest itemRequest) {
        log.info("ItemDto {}, User {} и Request {} в маппер", itemDto, user, itemRequest);
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .isAvailable(itemDto.getAvailable())
                .owner(user)
                .request(itemRequest)
                .build();
        log.info("Item из маппера: {}", item);
        return item;
    }

    public static ItemBookingDto mapToItemBookingDto(Item item, Booking lastBooking, Booking nextBooking,
                                                     List<CommentDto> commentDtos) {
        log.info("Item {} и Booking {} в маппер", item, lastBooking);
        ItemBookingDto itemBookingDto = ItemBookingDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailable())
                .lastBooking(lastBooking == null ? null : String.format("%s - %s",
                        lastBooking.getStartDate(), lastBooking.getEndDate()))
                .nextBooking(nextBooking == null ? null : String.format("%s - %s",
                        nextBooking.getStartDate(), nextBooking.getEndDate()))
                .comments(commentDtos)
                .build();
        log.info("ItemBookingDto из маппера: {}", itemBookingDto);
        return itemBookingDto;
    }

    public static List<ItemDto> mapToItemDto(List<Item> items) {
        log.info("Items в маппер: {}", items);
        List<ItemDto> itemDtos = items.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
        log.info("ItemDtos из маппера: {}", itemDtos);
        return itemDtos;
    }
}
