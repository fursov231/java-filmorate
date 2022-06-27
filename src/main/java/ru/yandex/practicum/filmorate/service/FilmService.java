package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeStorage likeStorage,
                       MpaStorage mpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeStorage = likeStorage;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    public Film add(Film newFilm) {
        if (newFilm.getDuration() < 0) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода продолжительности фильма");
            throw new ValidationException("Введена неверная продолжительность фильма");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Запрос на добавление фильма не выполнен из-за неверного ввода года выхода");
            throw new ValidationException("Введен неверный год выпуска");
        }
        return filmStorage.add(newFilm);
    }

    public Film update(Film updatedFilm) {
        if (updatedFilm.getId() <= 0) {
            throw new NotFoundException("Введен неверный id");
        }
        log.info("Запрос на обновление фильма выполнен");
        return filmStorage.update(updatedFilm);
    }

    public boolean delete(String name) {
        return filmStorage.delete(name);
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Optional<Film> findById(long id) {
        Optional<Film> film = filmStorage.findById(id);
        if (film.isEmpty()) {
            throw new NotFoundException("Неверно указан id");
        }
        return film;
    }

    public void like(long filmId, long userId) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new NotFoundException("Задан неверный filmId");
        } else if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Задан неверный userId");
        }
        if (likeStorage.isLikeByUser(filmId, userId)) {
            throw new NotFoundException("Фильму можно поставить оценку только один раз");
        }
        likeStorage.like(filmId, userId);
    }

    public void unlike(long filmId, long userId) {
        if (filmStorage.findById(filmId).isEmpty()) {
            throw new NotFoundException("Задан неверный filmId");
        } else if (userStorage.findById(userId).isEmpty()) {
            throw new NotFoundException("Задан неверный userId");
        }
        if (!likeStorage.isLikeByUser(filmId, userId)) {
            throw new NotFoundException("Отменить оценку нельзя ввиду ее отсутствия");
        }
        likeStorage.unlike(filmId, userId);
    }

    public List<Film> findPopulars(Integer count) {
        return filmStorage.findPopulars(count);
    }

    public Mpa getMpaById(long id) {
        if (id < 1 || id > 6) {
            throw new NotFoundException("Задан неверный mpa_id");
        }
        return mpaStorage.findMpaById(id);
    }

    public List<Mpa> findAllMpa() {
        return mpaStorage.findAllMpa();
    }

    public Genre getGenreById(long id) {
        if (id < 1 || id > 7) {
            throw new NotFoundException("Задан неверный genreId");
        }
        return genreStorage.findGenreById(id);
    }

    public List<Genre> findAllGenres() {
        return genreStorage.findAllGenres();
    }
}
