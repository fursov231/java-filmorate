package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Getter
    private final List<User> list = new ArrayList<>();
    private int id;

    @PostMapping
    public User add(@RequestBody @Valid User newUser) {
        if (newUser.getName().isEmpty()) {
            log.info("Имя присвоено по названию логина");
            newUser.setName(newUser.getLogin());
        }
        if (newUser.getLogin().chars().anyMatch(Character ::isWhitespace)) {
            log.info("Введен логин с пробелом");
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.info("Введен неверный год рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        newUser.setId(++id);
        list.add(newUser);
        log.info("Запрос на добавление пользователя выполнен");
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User updatedUser) {
        list.replaceAll(e -> e.getId() == updatedUser.getId() ? updatedUser : e);
        log.info("Запрос на обновление пользователя выполнен");
        return updatedUser;
    }

    @GetMapping
    public List<User> findAll() {
        return list;
    }
}


