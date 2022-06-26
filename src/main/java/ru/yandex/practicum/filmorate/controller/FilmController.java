package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Optional<Film> add(@RequestBody @Valid Film newFilm) {
        return filmService.add(newFilm);
    }

    @PutMapping("/films")
    public Film update(@RequestBody @Valid Film updatedFilm) {
        return filmService.update(updatedFilm);
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Optional<Film> findById(@PathVariable long id) {
        return filmService.findById(id);
    }

    @DeleteMapping("/films/{name}")
    public ResponseEntity delete(@PathVariable String name) {
        if (filmService.delete(name)) {
            return new ResponseEntity("Фильм успешно удален");
        } else {
            return new ResponseEntity("Неудачная попытка удаления фильма");
        }
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public ResponseEntity like(@PathVariable long filmId, @PathVariable long userId) {
        boolean isLike = filmService.like(filmId, userId);
        if (isLike) {
            return new ResponseEntity("Лайк успешно поставлен");
        } else {
            return new ResponseEntity("Неудачная попытка поставить лайк");
        }
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public ResponseEntity unlike(@PathVariable long filmId, @PathVariable long userId) {
        boolean isUnlike = filmService.unlike(filmId, userId);
        if (isUnlike) {
            return new ResponseEntity("Лайк успешно удален");
        } else {
            return new ResponseEntity("Неудачная попытка отмены лайка");
        }
    }

    @GetMapping("/films/popular")
    public List<Film> findPopulars(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.findPopulars(count);
    }

    @GetMapping("/mpa/{id}")
    public Mpa findMpaById(@PathVariable long id) {
        return filmService.getMpaById(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> findAllMpa() {
        return filmService.findAllMpa();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable long id) {
        return filmService.getGenreById(id);
    }

    @GetMapping("/genres")
    public List<Genre> findAllGenres() {
        return filmService.findAllGenres();
    }
}

