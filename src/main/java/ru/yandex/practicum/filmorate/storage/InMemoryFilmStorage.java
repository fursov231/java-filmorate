package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final List<Film> list = new ArrayList<>();
    private int id;

    @Override
    public void add(Film newFilm) {
        newFilm.setId(++id);
        list.add(newFilm);
    }

    @Override
    public void update(Film updatedFilm) {
        list.replaceAll(e -> e.getId() == updatedFilm.getId() ? updatedFilm : e);
    }

    @Override
    public List<Film> findAll() {
        return list;
    }

    @Override
    public Film findById(long id) {
        return list.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
    }
}
