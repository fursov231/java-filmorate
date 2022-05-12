package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final List<Film> list = new ArrayList<>();
    private int id;

    @PostMapping
    public Film add(@RequestBody @Valid Film newFilm) {
        if (newFilm.getDuration().isNegative()) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода продолжительности фильма");
            throw new ValidationException("Введена неверная продолжительность фильма");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода года выхода");
            throw new ValidationException("Введен неверный год выпуска");
        }
        newFilm.setId(++id);
        list.add(newFilm);
        log.info("Запрос на добавление фильма выполнен");
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film updatedFilm) {
        list.replaceAll(e -> e.getId() == updatedFilm.getId() ? updatedFilm : e);
        log.info("Запрос на обновление фильма выполнен");
        return updatedFilm;
    }

    @GetMapping
    public List<Film> findAll() {
        return list;
    }
}

