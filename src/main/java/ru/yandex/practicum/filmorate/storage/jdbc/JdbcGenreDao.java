package ru.yandex.practicum.filmorate.storage.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
public class JdbcGenreDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public JdbcGenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findGenreById(long id) {
        String sql = String.format("SELECT GENRE_ID, NAME " +
                "FROM GENRE " +
                "WHERE GENRE_ID=%d", id);
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name"))).get(0);
    }

    @Override
    public List<Genre> findAllGenres() {
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("name")));
    }

    @Override
    public List<Genre> findFilmsGenres(long filmId) {
        String sql = String.format("SELECT FG.GENRE_ID AS GENRE_ID, G.NAME AS NAME " +
                "FROM FILM_GENRES AS FG " +
                "LEFT JOIN GENRE AS G ON FG.GENRE_ID = G.GENRE_ID " +
                "WHERE FG.FILM_ID=%d", filmId);
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(
                rs.getInt("genre_id"),
                rs.getString("name")
        ));
        if (genres.isEmpty()) {
            return null;
        } else {
            return genres;
        }
    }
}
