package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Getter
    private final Map<Integer, User> map = new HashMap<>();

    @PostMapping
    public void add(@RequestBody @Valid User newUser) {
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
        map.put(newUser.getId(), newUser);
        log.info("Запрос на добавление пользователя выполнен");
    }

    @PutMapping
    public void update(@RequestBody User updatedUser) {
        map.replace(updatedUser.getId(), updatedUser);
        log.info("Запрос на обновление пользователя выполнен");
    }

    @GetMapping
    public Map<Integer, User> findAll() {
        return map;
    }
}


