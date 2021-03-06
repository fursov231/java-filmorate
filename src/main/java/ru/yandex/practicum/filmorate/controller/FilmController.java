package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public Film add(@RequestBody @Valid Film newFilm) {
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
    public ResponseEntity<String> delete(@PathVariable String name) {
        if (filmService.delete(name)) {
            return new ResponseEntity<>("Фильм успешно удален", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Неудачная попытка удаления фильма", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public ResponseEntity<String> like(@PathVariable long filmId, @PathVariable long userId) {
        filmService.like(filmId, userId);
        return new ResponseEntity<>("Лайк успешно поставлен", HttpStatus.OK);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public ResponseEntity<String> unlike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.unlike(filmId, userId);
        return new ResponseEntity<>("Лайк успешно удален", HttpStatus.OK);
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

