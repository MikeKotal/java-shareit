package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto getUser(Long id);

    UserDto updateUser(Long id, UserDto user);

    void deleteUser(Long id);
}
