package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingService {

    BookingDto createBooking(Long userId, BookingRequestDto bookingDto);

    BookingDto approveBooking(Long userId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long userId, Long bookingId);

    List<BookingDto> getBookingsByBookerId(Long bookerId, Status state);

    List<BookingDto> getBookingsByOwnerId(Long ownerId, Status state);
}
