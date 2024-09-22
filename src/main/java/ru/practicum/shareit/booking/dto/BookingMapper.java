package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDto mapToBookingDto(Booking booking) {
        log.info("Booking в маппер: {}", booking);
        BookingDto bookingDto = BookingDto.builder()
                .id(booking.getId())
                .start(getInstantToDate(booking.getStartDate()))
                .end(getInstantToDate(booking.getEndDate()))
                .status(booking.getStatus())
                .item(ItemMapper.mapToItemDto(booking.getItem()))
                .booker(UserMapper.mapToUserDto(booking.getBooker()))
                .build();
        log.info("BookingDto из маппера: {}", bookingDto);
        return bookingDto;
    }

    public static Booking mapToBooking(BookingDto bookingDto, Item item, User user) {
        log.info("BookingDto в маппер: {}", bookingDto);
        Booking booking = Booking.builder()
                .startDate(getDateToInstant(bookingDto.getStart()))
                .endDate(getDateToInstant(bookingDto.getEnd()))
                .item(item)
                .booker(user)
                .build();
        log.info("Booking из маппера: {}", booking);
        return booking;
    }

    public static List<BookingDto> mapToBookingDto(Iterable<Booking> bookings) {
        log.info("Bookings в маппер: {}", bookings);
        List<BookingDto> bookingDtos = StreamSupport.stream(bookings.spliterator(), false)
                .map(BookingMapper::mapToBookingDto)
                .toList();
        log.info("BookingDtos из маппера: {}", bookingDtos);
        return bookingDtos;
    }

    public static String getInstantToDate(Instant instant) {
        return instant
                .atZone(ZoneId.of("UTC"))
                .truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static Instant getDateToInstant(String date) {
        try {
            return LocalDateTime.parse(date).atOffset(ZoneOffset.UTC).toInstant();
        } catch (DateTimeParseException e) {
            throw new ValidationException("Некорректный формат времени");
        }
    }
}
