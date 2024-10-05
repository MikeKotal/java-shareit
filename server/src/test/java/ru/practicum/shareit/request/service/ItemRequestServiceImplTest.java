package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ShortItemDto;
import ru.practicum.shareit.request.dao.ItemRequestRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceImplTest {

    @Autowired
    ItemRequestService itemRequestService;

    @MockBean
    ItemRequestRepository itemRequestRepository;

    @MockBean
    ItemRepository itemRepository;

    @MockBean
    UserRepository userRepository;

    @Mock
    User user;

    @Mock
    ItemRequest itemRequest;

    @Mock
    ShortItemDto item;

    @Mock
    RequestDto requestDto;

    @Mock
    Page<ItemRequest> itemRequests;

    @BeforeEach
    public void setUp() {
        itemRequest.setRequester(user);
    }

    @Test
    public void createItemRequestTest() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        Mockito.when(itemRequestRepository.save(any())).thenReturn(itemRequest);
        itemRequestService.createItemRequest(1L, requestDto);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(itemRequestRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void getItemRequestsByRequesterIdTest() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(itemRequestRepository.findAllByRequesterId(anyLong(), any())).thenReturn(List.of(itemRequest));
        Mockito.when(itemRequest.getId()).thenReturn(1L);
        Mockito.when(itemRepository.findAllByRequestId(1L)).thenReturn(List.of(item));
        itemRequestService.getItemRequestsByRequesterId(1L);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findAllByRequesterId(anyLong(), any());
        Mockito.verify(itemRepository, Mockito.times(1)).findAllByRequestId(1L);
    }

    @Test
    public void getItemRequestsTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort. by(Sort.Direction. DESC, "created"));
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(itemRequestRepository.findAll(pageable)).thenReturn(itemRequests);
        Mockito.when(itemRequests.getContent()).thenReturn(List.of(itemRequest));
        Mockito.when(itemRequest.getId()).thenReturn(1L);
        Mockito.when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of(item));
        itemRequestService.getItemRequests(1L, 0);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(itemRequestRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(itemRepository, Mockito.times(1)).findAllByRequestId(1L);
    }

    @Test
    public void getItemRequestTest() {
        Mockito.when(userRepository.existsById(anyLong())).thenReturn(Boolean.TRUE);
        Mockito.when(itemRequestRepository.findById(1L)).thenReturn(Optional.ofNullable(itemRequest));
        Mockito.when(itemRepository.findAllByRequestId(1L)).thenReturn(List.of(item));
        itemRequestService.getItemRequest(1L, 1L);

        Mockito.verify(userRepository, Mockito.times(1)).existsById(1L);
        Mockito.verify(itemRequestRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(itemRepository, Mockito.times(1)).findAllByRequestId(1L);
    }
}
