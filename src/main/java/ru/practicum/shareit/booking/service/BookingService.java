package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(Long userId, BookingDto bookingDto);

    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getBookingsByBookerId(Long bookerId, String state);

    List<BookingDto> getBookingsByOwnerId(Long ownerId, String state);
}
