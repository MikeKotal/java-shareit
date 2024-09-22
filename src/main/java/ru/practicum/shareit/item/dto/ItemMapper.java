package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.StreamSupport;

import static ru.practicum.shareit.booking.dto.BookingMapper.getInstantToDate;

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

    public static Item mapToItem(ItemDto itemDto, User user) {
        log.info("ItemDto {} и User {} в маппер", itemDto, user);
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .isAvailable(itemDto.getAvailable())
                .owner(user)
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
                        getInstantToDate(lastBooking.getStartDate()), getInstantToDate(lastBooking.getEndDate())))
                .nextBooking(nextBooking == null ? null : String.format("%s - %s",
                        getInstantToDate(nextBooking.getStartDate()), getInstantToDate(nextBooking.getEndDate())))
                .comments(commentDtos)
                .build();
        log.info("ItemBookingDto из маппера: {}", itemBookingDto);
        return itemBookingDto;
    }

    public static List<ItemDto> mapToItemDto(Iterable<Item> items) {
        log.info("Items в маппер: {}", items);
        List<ItemDto> itemDtos = StreamSupport.stream(items.spliterator(), false)
                .map(ItemMapper::mapToItemDto)
                .toList();
        log.info("ItemDtos из маппера: {}", itemDtos);
        return itemDtos;
    }
}
