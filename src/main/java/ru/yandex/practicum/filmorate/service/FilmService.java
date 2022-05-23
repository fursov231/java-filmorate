package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private int id;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
        newFilm.setId(++id);
        filmStorage.add(newFilm);
        log.info("Запрос на добавление фильма выполнен");
        return newFilm;
    }

    public Film update(Film updatedFilm) {
        if (updatedFilm.getId() < 0) {
            throw new NotFoundException("Введен неверный id");
        }
        filmStorage.update(updatedFilm);
        log.info("Запрос на обновление фильма выполнен");
        return updatedFilm;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long id) {
        if (filmStorage.findById(id) == null) {
            throw new NotFoundException("Неверно указан id");
        }
        return filmStorage.findById(id);
    }

    public void like(long id, long userId) {
        if (filmStorage.findById(id) == null || filmStorage.findById(userId) == null) {
            throw new NotFoundException("Задан неверный id");
        }
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        film.like(user.getId());
        filmStorage.update(film);
        log.info("Лайк поставлен");
    }

    public void unlike(long id, long userId) {
        if (filmStorage.findById(id) == null || filmStorage.findById(userId) == null) {
            throw new NotFoundException("Задан неверный id");
        }
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);
        film.unlike(user.getId());
        filmStorage.update(film);
        log.info("Лайк удален");
    }

    public List<Film> showTop(Integer count) {
        List<Film> filmSet = filmStorage.findAll();
        filmSet.sort(Comparator.comparingLong(Film::getLikesCount).reversed());
        return filmSet.subList(0, count > filmSet.size() ? filmSet.size() : count);
    }
}
