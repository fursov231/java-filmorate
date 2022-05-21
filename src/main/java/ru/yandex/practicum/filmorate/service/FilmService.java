package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void like(long id, long userId) {
            Film film = filmStorage.findById(id);
            User user = userStorage.findById(userId);
            film.like(user.getId());
            filmStorage.update(film);
    }

    public void unlike(long id, long userId) {
            Film film = filmStorage.findById(id);
            User user = userStorage.findById(userId);
            film.unlike(user.getId());
            filmStorage.update(film);
    }

    public List<Film> showTop(Integer count) {
        List<Film> filmSet = filmStorage.findAll();
        filmSet.sort(Comparator.comparingLong(Film::getLikesCount).reversed());
        return filmSet.subList(0, count > filmSet.size() ? filmSet.size() : count);
    }
}
