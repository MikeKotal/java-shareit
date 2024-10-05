package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking) {
        log.info("Booking в маппер: {}", booking);
        BookingDto bookingDto = BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStartDate())
                .end(booking.getEndDate())
                .status(booking.getStatus())
                .item(ItemMapper.mapToItemDto(booking.getItem()))
                .booker(UserMapper.mapToUserDto(booking.getBooker()))
                .build();
        log.info("BookingDto из маппера: {}", bookingDto);
        return bookingDto;
    }

    public static Booking mapToBooking(BookingRequestDto bookingDto, Item item, User user) {
        log.info("BookingDto в маппер: {}", bookingDto);
        Booking booking = Booking.builder()
                .startDate(bookingDto.getStart())
                .endDate(bookingDto.getEnd())
                .item(item)
                .booker(user)
                .build();
        log.info("Booking из маппера: {}", booking);
        return booking;
    }

    public static List<BookingDto> mapToBookingDto(List<Booking> bookings) {
        log.info("Bookings в маппер: {}", bookings);
        List<BookingDto> bookingDtos = bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .toList();
        log.info("BookingDtos из маппера: {}", bookingDtos);
        return bookingDtos;
    }
}
