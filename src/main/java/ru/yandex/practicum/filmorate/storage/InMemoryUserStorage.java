package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final List<User> list = new ArrayList<>();
    private int id;

    @Override
    public void add(User newUser) {
        newUser.setId(++id);
        list.add(newUser);
    }

    @Override
    public void update(User updatedUser) {
        list.replaceAll(e -> e.getId() == updatedUser.getId() ? updatedUser : e);
    }

    @Override
    public List<User> findAll() {
        return list;
    }

    @Override
    public User findById(long id) {
        return list.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }
}
