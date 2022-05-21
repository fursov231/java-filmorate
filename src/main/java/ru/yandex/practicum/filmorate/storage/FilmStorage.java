package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    void add(Film newFilm);

    void update(Film updatedFilm);

    List<Film> findAll();

    Film findById(long id);
}
