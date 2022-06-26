package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Optional<User> add(User newUser) {
        if (newUser.getName().isEmpty()) {
            log.info("Имя присвоено по названию логина");
            newUser.setName(newUser.getLogin());
        }
        if (newUser.getLogin().chars().anyMatch(Character::isWhitespace)) {
            log.info("Введен логин с пробелом");
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.info("Введен неверный год рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        if (userStorage.findByEmail(newUser.getEmail()).isPresent()) {
            log.info("Попытка добавления пользователя с существующим email");
            throw new ValidationException("Такой email уже существует");
        }
        userStorage.add(newUser);
        log.info("Запрос на добавление пользователя выполнен");
        return userStorage.findByEmail(newUser.getEmail());
    }

    public User update(User updatedUser) {
        if (userStorage.findById(updatedUser.getId()).isEmpty()) {
            throw new NotFoundException("Передан неверный id");
        }
        userStorage.update(updatedUser);
        log.info("Запрос на обновление пользователя выполнен");
        return updatedUser;
    }

    public List<User> findAll() {
        log.info("Запрос на поиск всех пользователей");
        return userStorage.findAll();
    }

    public Optional<User> findById(long id) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Передан неверный id");
        }
        return userStorage.findById(id);
    }

    public void addAsFriend(long id, long friendId) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче userId");
        } else if (userStorage.findById(friendId).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче friendId");
        }
        if (id == friendId) {
            throw new ValidationException("Добавление самого себя в друзья невозможно");
        }
        userStorage.addAsFriend(id, friendId);
        log.info("Выполнен запрос на добавление в друзья");
    }

    public boolean confirmAddingAsFriend(long id, long friendId) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче userId");
        } else if (userStorage.findById(friendId).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче friendId");
        }
        if (userStorage.findFriendRequest(friendId, id)) {
            userStorage.confirmAddingAsFriend(id, friendId);
            log.info("Выполнен запрос на подтверждение дружбы");
            return true;
        } else {
            log.info("Запрос на подтверждение дружбы не выполнен");
            return false;
        }

    }

    public void removeFriend(long id, long friendId) {
        if (userStorage.findById(id).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче userId");
        } else if (userStorage.findById(friendId).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче friendId");
        }
        if (id == friendId) {
            throw new ValidationException("Удаление самого себя из друзей невозможно");
        }
        userStorage.removeFromFriends(id, friendId);
        log.info("Выполнен запрос на удаление из друзей");
    }

    public List<User> findUsersFriends(long userId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче userId");
        }
        return userStorage.findUsersFriends(userId);
    }

    public List<User> findCommonUsersFriends(long userId, long friendId) {
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче userId");
        } else if (userStorage.findById(friendId).isEmpty()) {
            throw new NotFoundException("Ошибка в передаче friendId");
        }
        return userStorage.findCommonUsersFriends(userId, friendId);
    }
}
