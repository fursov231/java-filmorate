package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film newFilm);

    Film update(Film updatedFilm);

    boolean delete(String name);

    List<Film> findAll();

    Optional<Film> findById(long id);

    List<Film> findPopulars(Integer count);
}
