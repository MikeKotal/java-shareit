package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;

    @Mock
    User user;

    @Mock
    UserRequestDto userRequestDto;

    @Test
    public void createUserTest() {
        Mockito.when(userRepository.save(any())).thenReturn(user);
        userService.createUser(userRequestDto);

        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void getUserTest() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        userService.getUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    public void updateUserTest() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.save(any())).thenReturn(user);
        userService.updateUser(1L, userRequestDto);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).save(any());
    }

    @Test
    public void deleteUserTest() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        Mockito.doNothing().when(userRepository).delete(any());
        userService.deleteUser(1L);

        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Mockito.verify(userRepository, Mockito.times(1)).delete(any());
    }
}
