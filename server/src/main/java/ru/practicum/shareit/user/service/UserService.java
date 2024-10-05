package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;

public interface UserService {

    UserDto createUser(UserRequestDto user);

    UserDto getUser(Long id);

    UserDto updateUser(Long id, UserRequestDto user);

    void deleteUser(Long id);
}
