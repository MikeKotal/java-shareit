package ru.practicum.shareit.item.service;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemReqDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @Autowired
    ItemService itemService;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    BookingRepository bookingRepository;

    @MockBean
    CommentRepository commentRepository;

    @MockBean
    ItemRequestRepository itemRequestRepository;

    @Mock
    Booking booking;

    @Mock
    User user;

    @Mock
    Item item;

    @Mock
    ItemReqDto itemReqDto;

    @Mock
    ItemRequest itemRequest;

    @Mock
    Comment comment;

    @Mock
    CommentRequestDto commentRequestDto;

    @BeforeEach
    public void setUp() {
        booking.setBooker(user);
        booking.setItem(item);
        item.setOwner(user);
    }

    @Test
    public void createItemTest() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(itemReqDto.getRequestId()).thenReturn(1L);
        Mockito.when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(itemRequest));
        Mockito.when(itemRepository.save(any())).thenReturn(item);
        itemService.createItem(1L, itemReqDto);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(itemReqDto, Mockito.times(2)).getRequestId();
        Mockito.verify(itemRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void getItemTest() {
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        Mockito.when(commentRepository.findAllByItemId(anyLong())).thenReturn(new ArrayList<>());
        Mockito.when(bookingRepository.findFirstByItemIdAndItemOwnerIdAndEndDateBefore(anyLong(), anyLong(),
                any(LocalDateTime.class), any())).thenReturn(booking);
        Mockito.when(bookingRepository.findFirstByItemIdAndItemOwnerIdAndStartDateAfter(anyLong(), anyLong(),
                any(LocalDateTime.class), any())).thenReturn(booking);
        itemService.getItem(1L, 1L);

        Mockito.verify(itemRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(commentRepository, Mockito.times(1)).findAllByItemId(1L);
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findFirstByItemIdAndItemOwnerIdAndEndDateBefore(anyLong(), anyLong(), any(LocalDateTime.class), any());
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findFirstByItemIdAndItemOwnerIdAndStartDateAfter(anyLong(), anyLong(), any(LocalDateTime.class), any());
    }

    @Test
    public void getItemsTest() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(item.getOwner()).thenReturn(user);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(item.getId()).thenReturn(1L);
        Mockito.when(itemRepository.findAllByOwnerId(anyLong())).thenReturn(List.of(item));
        Mockito.when(bookingRepository.findFirstByItemIdAndItemOwnerIdAndEndDateBefore(anyLong(), anyLong(),
                any(LocalDateTime.class), any())).thenReturn(booking);
        Mockito.when(bookingRepository.findFirstByItemIdAndItemOwnerIdAndStartDateAfter(anyLong(), anyLong(),
                any(LocalDateTime.class), any())).thenReturn(booking);
        Mockito.when(commentRepository.findAllByItemId(anyLong())).thenReturn(new ArrayList<>());
        itemService.getItems(1L);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(itemRepository, Mockito.times(1)).findAllByOwnerId(1L);
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findFirstByItemIdAndItemOwnerIdAndEndDateBefore(anyLong(), anyLong(), any(LocalDateTime.class), any());
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findFirstByItemIdAndItemOwnerIdAndStartDateAfter(anyLong(), anyLong(), any(LocalDateTime.class), any());
        Mockito.verify(commentRepository, Mockito.times(1)).findAllByItemId(1L);
    }

    @Test
    public void getItemsByTextTest() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(itemRepository.findItemsByText(anyString())).thenReturn(List.of(item));
        itemService.getItemsByText(1L, "Test");

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(itemRepository, Mockito.times(1)).findItemsByText("Test");
    }

    @Test
    public void updateItemTest() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        Mockito.when(item.getOwner()).thenReturn(user);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(itemRepository.save(any())).thenReturn(item);
        itemService.updateItem(1L, 1L, itemReqDto);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(itemRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(itemRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void createCommentTest() {
        Mockito.when(bookingRepository.findFirstByBookerIdAndItemIdAndEndDateBefore(anyLong(), anyLong(),
                any(LocalDateTime.class))).thenReturn(Optional.ofNullable(booking));
        Mockito.when(commentRepository.save(any())).thenReturn(comment);
        Mockito.when(booking.getItem()).thenReturn(item);
        Mockito.when(booking.getBooker()).thenReturn(user);
        Mockito.when(comment.getAuthor()).thenReturn(user);
        itemService.createComment(1L, 1L, commentRequestDto);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findFirstByBookerIdAndItemIdAndEndDateBefore(anyLong(), anyLong(), any(LocalDateTime.class));
        Mockito.verify(commentRepository, Mockito.times(1)).save(any());
    }
}
