package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    @Autowired
    BookingService bookingService;

    @MockBean
    BookingRepository bookingRepository;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    UserRepository userRepository;

    @Mock
    Booking booking;

    @Mock
    User user;

    @Mock
    Item item;

    @Mock
    BookingRequestDto bookingRequestDto;

    @BeforeEach
    public void setUp() {
        booking.setBooker(user);
        booking.setItem(item);
        item.setOwner(user);
    }

    @Test
    public void createBookingTest() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(bookingRequestDto.getItemId()).thenReturn(1L);
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        Mockito.when(item.getIsAvailable()).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRequestDto.getStart()).thenReturn(LocalDateTime.now());
        Mockito.when(bookingRequestDto.getEnd()).thenReturn(LocalDateTime.now().plusDays(1L));
        Mockito.when(item.getId()).thenReturn(1L);
        Mockito.when(booking.getItem()).thenReturn(item);
        Mockito.when(booking.getBooker()).thenReturn(user);
        Mockito.when(bookingRepository.findBookingByItemIdAndDate(anyLong(), any(), any())).thenReturn(new ArrayList<>());
        Mockito.when(bookingRepository.save(any())).thenReturn(booking);
        bookingService.createBooking(1L, bookingRequestDto);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(itemRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findBookingByItemIdAndDate(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class));
        Mockito.verify(bookingRepository, Mockito.times(1)).save(any());
        Mockito.verify(booking, Mockito.times(1)).getStartDate();
        Mockito.verify(booking, Mockito.times(1)).getEndDate();
    }

    @Test
    public void approveBookingTest() {
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(booking.getBooker()).thenReturn(user);
        Mockito.when(booking.getItem()).thenReturn(item);
        Mockito.when(item.getOwner()).thenReturn(user);
        Mockito.when(booking.getStatus()).thenReturn(Status.WAITING);
        Mockito.doNothing().when(booking).setStatus(any());
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(bookingRepository.save(any())).thenReturn(booking);
        bookingService.approveBooking(1L, 1L, Boolean.TRUE);

        Mockito.verify(bookingRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void getBookingTest() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Mockito.when(booking.getBooker()).thenReturn(user);
        Mockito.when(booking.getItem()).thenReturn(item);
        Mockito.when(user.getId()).thenReturn(1L);
        bookingService.getBooking(1L, 1L);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(booking, Mockito.times(2)).getBooker();
        Mockito.verify(booking, Mockito.times(1)).getItem();
        Mockito.verify(user, Mockito.times(2)).getId();
    }

    @Test
    public void getAllBookingsByBookerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByBookerId(anyLong(), any())).thenReturn(new ArrayList<>());
        bookingService.getBookingsByBookerId(1L, Status.ALL);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByBookerId(anyLong(), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getWaitingBookingsByBookerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(Status.class), any()))
                .thenReturn(new ArrayList<>());
        bookingService.getBookingsByBookerId(1L, Status.WAITING);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByBookerIdAndStatus(anyLong(),
                any(Status.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getRejectedBookingsByBookerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByBookerIdAndStatus(anyLong(), any(Status.class), any()))
                .thenReturn(new ArrayList<>());
        bookingService.getBookingsByBookerId(1L, Status.REJECTED);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByBookerIdAndStatus(anyLong(),
                any(Status.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getFutureBookingsByBookerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByBookerIdAndStartDateAfter(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(new ArrayList<>());
        bookingService.getBookingsByBookerId(1L, Status.FUTURE);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByBookerIdAndStartDateAfter(anyLong(),
                any(LocalDateTime.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getPastBookingsByBookerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByBookerIdAndEndDateBefore(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(new ArrayList<>());
        bookingService.getBookingsByBookerId(1L, Status.PAST);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByBookerIdAndEndDateBefore(anyLong(),
                any(LocalDateTime.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getCurrentBookingsByBookerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByBookerIdAndStartDateBeforeAndEndDateAfter(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any())).thenReturn(new ArrayList<>());
        bookingService.getBookingsByBookerId(1L, Status.CURRENT);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerIdAndStartDateBeforeAndEndDateAfter(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getAllBookingsByOwnerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByItemOwnerId(anyLong(), any())).thenReturn(new ArrayList<>());
        bookingService.getBookingsByOwnerId(1L, Status.ALL);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByItemOwnerId(anyLong(), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getWaitingBookingsByOwnerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(), any(Status.class), any()))
                .thenReturn(new ArrayList<>());
        bookingService.getBookingsByOwnerId(1L, Status.WAITING);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByItemOwnerIdAndStatus(anyLong(),
                any(Status.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getRejectedBookingsByOwnerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStatus(anyLong(), any(Status.class), any()))
                .thenReturn(new ArrayList<>());
        bookingService.getBookingsByOwnerId(1L, Status.REJECTED);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByItemOwnerIdAndStatus(anyLong(),
                any(Status.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getFutureBookingsByOwnerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartDateAfter(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(new ArrayList<>());
        bookingService.getBookingsByOwnerId(1L, Status.FUTURE);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByItemOwnerIdAndStartDateAfter(anyLong(),
                any(LocalDateTime.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getPastBookingsByOwnerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndEndDateBefore(anyLong(), any(LocalDateTime.class), any()))
                .thenReturn(new ArrayList<>());
        bookingService.getBookingsByOwnerId(1L, Status.PAST);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1)).findAllByItemOwnerIdAndEndDateBefore(anyLong(),
                any(LocalDateTime.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }

    @Test
    public void getCurrentBookingsByOwnerId() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfter(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any())).thenReturn(new ArrayList<>());
        bookingService.getBookingsByOwnerId(1L, Status.CURRENT);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByItemOwnerIdAndStartDateBeforeAndEndDateAfter(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class), any());
        Assertions.assertEquals(1, Mockito.mockingDetails(bookingRepository).getInvocations().size(),
                "Объект bookingRepository должен был быть вызван ровно 1 раз");
    }
}
