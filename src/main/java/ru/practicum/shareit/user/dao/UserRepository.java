package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserRepository {

    User addUser(User user);

    Optional<User> findUser(Long id);

    User updateUser(User user);

    void removeUser(Long id);

    Boolean checkUserExists(Long id);
}