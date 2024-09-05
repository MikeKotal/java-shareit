package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicationException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        if (users.containsValue(user)) {
            throw new DuplicationException(String.format("Пользователь с email '%s' уже существует", user.getEmail()));
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User updateUser(User user) {
        if (users.values().stream().anyMatch(current -> !Objects.equals(current.getId(), user.getId())
                && current.equals(user))) {
            throw new DuplicationException(String.format("Пользователь с email '%s' уже существует", user.getEmail()));
        }
        User updatedUser = users.get(user.getId());
        updatedUser.setName(user.getName());
        updatedUser.setEmail(user.getEmail());
        return updatedUser;
    }

    @Override
    public void removeUser(Long id) {
        users.remove(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Boolean checkUserExists(Long id) {
        return users.containsKey(id);
    }
}
