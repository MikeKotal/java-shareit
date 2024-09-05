package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto user) {
        log.info("Запрос на создание пользователя {}", user);
        User newUser = UserMapper.mapToUser(user);
        newUser = userRepository.addUser(newUser);
        log.info("Пользователь успешно создан {}", newUser);
        return UserMapper.mapToUserDto(newUser);
    }

    @Override
    public UserDto getUser(Long id) {
        log.info("Запрос на получение пользователя с id {}", id);
        UserDto userDto = UserMapper.mapToUserDto(getUserById(id));
        log.info("Пользователь с id '{} = '{}'", id, userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(Long id, UserDto user) {
        log.info("Запрос на обновление пользователя с id {}", id);
        User oldUser = getUserById(id);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        oldUser = userRepository.updateUser(oldUser);
        log.info("Пользователь {} успешно обновлен", oldUser);
        return UserMapper.mapToUserDto(oldUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Запрос на удаление пользователя с id {}", id);
        getUserById(id);
        userRepository.removeUser(id);
        log.info("Пользователь с id {} удален", id);
    }

    private User getUserById(Long id) {
        return userRepository.findUser(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} отсутствует", id);
                    return new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено", id));
                });
    }
}
