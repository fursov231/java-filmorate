package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {
    final private UserStorage userStorage;
    final private UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User add(@RequestBody @Valid User newUser) {
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
        userStorage.add(newUser);
        log.info("Запрос на добавление пользователя выполнен");
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody @Valid User updatedUser) {
        if (userStorage.findById(updatedUser.getId()) == null) {
            throw new NotFoundException("Передан неверный id");
        }
        userStorage.update(updatedUser);
        log.info("Запрос на обновление пользователя выполнен");
        return updatedUser;
    }

    @GetMapping
    public List<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable long id) {
        if (userStorage.findById(id) == null) {
            throw new NotFoundException("Передан неверный id");
        }
        return userStorage.findById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addAsFriend(@PathVariable long id, @PathVariable long friendId) {
        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
        userService.addAsFriend(id, friendId);
        log.info("Запрос на добавление в друзья выполнен");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriend(@PathVariable long id, @PathVariable long friendId) {
        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
        userService.removeFriend(userStorage.findById(id), userStorage.findById(friendId));
        log.info("Запрос на удаление из друзей выполнен");
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable long id) {
        if (userStorage.findById(id) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
        return userService.findUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> showMutualUserFriends(@PathVariable long id, @PathVariable long otherId) {
        if (userStorage.findById(id) == null || userStorage.findById(otherId) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
        return userService.showMutualUserFriends(id, otherId);
    }
}


