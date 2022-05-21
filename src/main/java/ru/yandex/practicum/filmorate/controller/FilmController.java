package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;


    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film add(@RequestBody @Valid Film newFilm) {
        if (newFilm.getDuration() < 0) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода продолжительности фильма");
            throw new ValidationException("Введена неверная продолжительность фильма");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода года выхода");
            throw new ValidationException("Введен неверный год выпуска");
        }
        filmStorage.add(newFilm);
        log.info("Запрос на добавление фильма выполнен");
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film updatedFilm) {
        if (updatedFilm.getId() < 0) {
            throw new NotFoundException("Введен неверный id");
        }
        filmStorage.update(updatedFilm);
        log.info("Запрос на обновление фильма выполнен");
        return updatedFilm;
    }

    @GetMapping
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable long id) {
        if (filmStorage.findById(id) == null) {
            throw new NotFoundException("Неверно указан id");
        }
        return filmStorage.findById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable long id, @PathVariable long userId) {
        if (filmStorage.findById(id) == null || filmStorage.findById(userId) == null) {
            throw new NotFoundException("Задан неверный id");
        }
        filmService.like(id, userId);
        log.info("Лайк поставлен");
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void unlike(@PathVariable long id, @PathVariable long userId) {
        if (filmStorage.findById(id) == null || filmStorage.findById(userId) == null) {
            throw new NotFoundException("Задан неверный id");
        }
        filmService.unlike(id, userId);
        log.info("Лайк удален");
    }

    @GetMapping("/popular")
    public List<Film> showTop(@RequestParam (defaultValue = "10") Integer count) {
        return filmService.showTop(count);
    }
}

