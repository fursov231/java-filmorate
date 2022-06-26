package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film newFilm);

    Film update(Film updatedFilm);

    boolean delete(String name);

    List<Film> findAll();

    Optional<Film> findById(long id);

    boolean like(long userId, long filmId);

    boolean unlike(long userId, long filmId);

    List<Film> findPopulars(Integer count);

    Mpa findMpaById(long id);

    List<Mpa> findAllMpa();

    Genre findGenreById(long id);

    List<Genre> findAllGenres();
}
