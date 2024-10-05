package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    public static final Sort ORDER_BY_START_DATE = Sort.by(Sort.Direction.DESC, "startDate");

    @Override
    @Transactional
    public BookingDto createBooking(Long userId, BookingRequestDto bookingDto) {
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
        if (booking.getStatus() != Status.WAITING) {
            throw new ValidationException("Для подтверждения бронирования статус должен быть 'WAITING'");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        booking = bookingRepository.save(booking);
        log.info("Установлен статус {} для бронирования {}", booking.getStatus(), booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        log.info("Запрос от пользователя {} на просмотр бронирования {}", userId, bookingId);
        checkUserExists(userId);
        Booking booking = getBookingById(bookingId);
        if ((!booking.getBooker().getId().equals(userId))
                && (!booking.getItem().getOwner().getId().equals(userId))) {
            throw new ValidationException("Просмотр бронирования доступен только владельцу вещи или автору бронирования");
        }
        log.info("Бронирование {}", booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByBookerId(Long bookerId, Status state) {
        log.info("Запрос от пользователя {} на получение списка бронирований по фильтру {}", bookerId, state);
        checkUserExists(bookerId);
        LocalDateTime current = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByBookerId(bookerId, ORDER_BY_START_DATE);
            case WAITING, REJECTED ->
                    bookingRepository.findAllByBookerIdAndStatus(bookerId, state, ORDER_BY_START_DATE);
            case FUTURE -> bookingRepository.findAllByBookerIdAndStartDateAfter(bookerId, current, ORDER_BY_START_DATE);
            case PAST -> bookingRepository.findAllByBookerIdAndEndDateBefore(bookerId, current, ORDER_BY_START_DATE);
            case CURRENT -> bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfter(bookerId,
                    current, current, ORDER_BY_START_DATE);
            default -> throw new ValidationException(String.format("Несуществующий тип фильтра поиска %s", state));
        };
        log.info("Список бронирований {} пользователя", bookings);
        return BookingMapper.mapToBookingDto(bookings);
    }

    @Override
    public List<BookingDto> getBookingsByOwnerId(Long ownerId, Status state) {
        log.info("Запрос от владельца {} на получение списка бронирований по фильтру {}", ownerId, state);
        checkUserExists(ownerId);
        LocalDateTime current = LocalDateTime.now();
        List<Booking> bookings = switch (state) {
            case ALL -> bookingRepository.findAllByItemOwnerId(ownerId, ORDER_BY_START_DATE);
            case WAITING, REJECTED ->
                    bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, state, ORDER_BY_START_DATE);
            case FUTURE ->
                    bookingRepository.findAllByItemOwnerIdAndStartDateAfter(ownerId, current, ORDER_BY_START_DATE);
            case PAST -> bookingRepository.findAllByItemOwnerIdAndEndDateBefore(ownerId, current, ORDER_BY_START_DATE);
            case CURRENT -> bookingRepository.findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfter(ownerId,
                    current, current, ORDER_BY_START_DATE);
            default -> throw new ValidationException(String.format("Несуществующий тип фильтра поиска %s", state));
        };
        log.info("Список бронирований {} владельца", bookings);
        return BookingMapper.mapToBookingDto(bookings);
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
