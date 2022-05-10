package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@Slf4j
@RequestMapping("/film")
public class FilmController {
    @Getter
    private final Map<Integer, Film> map = new HashMap<>();

    @PostMapping
    public void add(@RequestBody @Valid Film newFilm) {
        if (newFilm.getDuration().isNegative()) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода продолжительности фильма");
            throw new ValidationException("Введена неверная продолжительность фильма");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода года выхода");
            throw new ValidationException("Введен неверный год выпуска");
        }
        map.put(newFilm.getId(), newFilm);
        log.info("Запрос на добавление фильма выполнен");
    }

    @PutMapping
    public void update(@RequestBody Film updatedFilm) {
        map.replace(updatedFilm.getId(), updatedFilm);
        log.info("Запрос на обновление фильма выполнен");
    }

    @GetMapping
    public Map<Integer, Film> findAll() {
        return map;
    }
}

