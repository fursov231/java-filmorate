package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre findGenreById(long id);

    List<Genre> findAllGenres();

    List<Genre> findFilmsGenres(long filmId);
}
