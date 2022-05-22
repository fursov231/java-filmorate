package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void add(User newUser);

    void update(User updatedUser);

    void delete(User user);

    List<User> findAll();

    User findById(long id);
}
