package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserRequestDto user) {
        log.info("Запрос на создание пользователя {}", user);
        User newUser = userRepository.save(UserMapper.mapToUser(user));
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
    @Transactional
    public UserDto updateUser(Long id, UserRequestDto user) {
        log.info("Запрос на обновление пользователя с id {}", id);
        User oldUser = getUserById(id);
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        oldUser = userRepository.save(oldUser);
        log.info("Пользователь {} успешно обновлен", oldUser);
        return UserMapper.mapToUserDto(oldUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Запрос на удаление пользователя с id {}", id);
        User user = getUserById(id);
        userRepository.delete(user);
        log.info("Пользователь с id {} удален", id);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Пользователь с id {} отсутствует", id);
                    return new NotFoundException(String.format("Пользователя с идентификатором = '%s' не найдено", id));
                });
    }
}
