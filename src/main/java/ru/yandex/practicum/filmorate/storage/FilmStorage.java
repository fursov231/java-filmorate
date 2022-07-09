package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FilmStorage {

    Film add(Film newFilm);

    Film update(Film updatedFilm);

    boolean delete(String name);

    List<Film> findAll();

    Optional<Film> findById(long id);

    List<Film> findPopulars(Integer count);

    /**
     * Get user likes for recommendation algorithm.
     * @return Map of the user's like IDs to a Set of film IDs.
     */
    Map<Integer, Set<Integer>> getUserLikes();
}
