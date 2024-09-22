package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        log.info("Запрос от пользователя {} на бронирование вещи {}", userId, bookingDto);
        User user = getUserById(userId);
        Item item = getItemById(bookingDto.getItemId());
        if (!item.getIsAvailable()) {
            throw new ValidationException("Вещь недоступна для бронирования");
        }
        Booking booking = BookingMapper.mapToBooking(bookingDto, item, user);
        if (booking.getStartDate().equals(booking.getEndDate())
                || booking.getEndDate().isBefore(booking.getStartDate())) {
            throw new ValidationException("Даты не могут быть равны или дата окончания не может быть раньше даты начала");
        }
        List<Booking> bookings = bookingRepository.findBookingByItemIdAndDate(item.getId(),
                booking.getStartDate(), booking.getEndDate());
        if (!bookings.isEmpty()) {
            throw new ValidationException("Бронирование невозможно, пересечение по датам бронирования");
        }
        booking.setStatus(Status.WAITING);
        booking = bookingRepository.save(booking);
        log.info("Вещь успешно забронирована: {}", booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long userId, Long bookingId, Boolean approved) {
        log.info("Запрос от пользователя {} на подтверждение бронирования {}", userId, bookingId);
        Booking booking = getBookingById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ValidationException("Подтвердить бронирование может только собственник вещи");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        booking = bookingRepository.save(booking);
        log.info("Установлен статус {} для бронирования {}", booking.getStatus(), booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        log.info("Запрос от пользователя {} на просмотр бронирования {}", userId, bookingId);
        Booking booking = getBookingById(bookingId);
        if ((!booking.getBooker().getId().equals(userId))
                && (!booking.getItem().getOwner().getId().equals(userId))) {
            throw new ValidationException("Просмотр бронирования доступен только владельцу вещи или автору бронирования");
        }
        log.info("Бронирование {}", booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(Long bookerId, String state) {
        log.info("Запрос от пользователя {} на получение списка бронирований по фильтру {}", bookerId, state);
        List<Booking> bookings = getBookings(bookerId, state, Boolean.TRUE);
        log.info("Список бронирований {} пользователя", bookings);
        return BookingMapper.mapToBookingDto(bookings);
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(Long ownerId, String state) {
        log.info("Запрос от владельца {} на получение списка бронирований по фильтру {}", ownerId, state);
        List<Booking> bookings = getBookings(ownerId, state, Boolean.FALSE);
        log.info("Список бронирований {} владельца", bookings);
        return BookingMapper.mapToBookingDto(bookings);
    }

    private List<Booking> getBookings(Long userId, String state, Boolean isBooker) {
        checkUserExists(userId);
        Instant current = Instant.now();
        return switch (Status.valueOf(state)) {
            case ALL -> isBooker ? bookingRepository.findAllByBookerIdOrderByStartDateDesc(userId)
                    : bookingRepository.findAllByItemOwnerIdOrderByStartDateDesc(userId);
            case WAITING, REJECTED ->
                    isBooker ? bookingRepository.findAllByBookerIdAndStatusOrderByStartDateDesc(userId,
                            Status.valueOf(state))
                            : bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDateDesc(userId,
                            Status.valueOf(state));
            case FUTURE ->
                    isBooker ? bookingRepository.findAllByBookerIdAndStartDateAfterOrderByStartDateDesc(userId,
                            Instant.now())
                            : bookingRepository.findAllByItemOwnerIdAndStartDateAfterOrderByStartDateDesc(userId,
                            Instant.now());
            case PAST ->
                    isBooker ? bookingRepository.findAllByBookerIdAndEndDateBeforeOrderByStartDateDesc(userId,
                            Instant.now())
                            : bookingRepository.findAllByItemOwnerIdAndEndDateBeforeOrderByStartDateDesc(userId,
                            Instant.now());
            case CURRENT ->
                    isBooker ? bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(userId,
                            current, current)
                            : bookingRepository.findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfterOrderByStartDateDesc(userId,
                            current, current);
            default -> throw new ValidationException(String.format("Несуществующий тип фильтра поиска %s", state));
        };
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

    private Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Бронирование с id {} отсутствует", id);
                    return new NotFoundException(String.format("Бронирование с идентификатором = '%s' не найдено", id));
                });
    }
}
