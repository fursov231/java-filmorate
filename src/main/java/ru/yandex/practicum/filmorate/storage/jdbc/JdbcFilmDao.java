package ru.yandex.practicum.filmorate.storage.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.util.UtilMapper;

import java.sql.Timestamp;
import java.util.*;

@Component
public class JdbcFilmDao implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(JdbcUserDao.class);
    private final JdbcTemplate jdbcTemplate;
    private final UtilMapper utilMapper;
    private final GenreStorage genreStorage;

    public JdbcFilmDao(JdbcTemplate jdbcTemplate, UtilMapper utilMapper, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilMapper = utilMapper;
        this.genreStorage = genreStorage;
    }

    @Override
    public Film add(Film newFilm) {
        String sql = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID) " +
                "VALUES (?, ?, ?, ?, ?, (SELECT MPA.MPA_ID FROM MPA WHERE MPA.MPA_ID=?))";
        jdbcTemplate.update(sql,
                newFilm.getName(),
                newFilm.getDescription(),
                Timestamp.valueOf(newFilm.getReleaseDate().atStartOfDay()),
                String.valueOf(newFilm.getDuration()),
                newFilm.getRate(),
                newFilm.getMpa().getId()
        );
        try {
            if (!newFilm.getGenres().isEmpty()) {
                Iterator<Genre> it = newFilm.getGenres().iterator();
                while (it.hasNext()) {
                    jdbcTemplate.update("INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) " +
                            "VALUES (SELECT FILM_ID " +
                            "FROM FILMS WHERE NAME=?, ?)", newFilm.getName(), it.next().getId());
                }
            }
        } catch (NullPointerException e) {
            log.info("У фильма {} не указаны жанры", newFilm.getName());
        }
        log.info("Фильм {} добавлен в БД", newFilm.getName());
        newFilm.setId(findIdByName(newFilm.getName()));
        return newFilm;
    }

    @Override
    public Film update(Film updatedFilm) {
        String sql = "UPDATE FILMS " +
                "SET NAME=?, RELEASE_DATE=?, DESCRIPTION=?, DURATION=?, RATE=?, MPA_ID=? " +
                "WHERE FILM_ID=?;";
        jdbcTemplate.update(sql,
                updatedFilm.getName(),
                Timestamp.valueOf(updatedFilm.getReleaseDate().atStartOfDay()),
                updatedFilm.getDescription(),
                String.valueOf(updatedFilm.getDuration()),
                updatedFilm.getRate(),
                updatedFilm.getMpa().getId(),
                updatedFilm.getId()
        );
        try {
            if (!updatedFilm.getGenres().isEmpty()) {
                jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID=?", updatedFilm.getId());
                Set<Genre> genresSet = new LinkedHashSet<>(updatedFilm.getGenres());
                updatedFilm.setGenres(new ArrayList<>(genresSet));
                Iterator<Genre> it = genresSet.iterator();
                while (it.hasNext()) {
                    jdbcTemplate.update("INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) " +
                            "VALUES (SELECT FILM_ID " +
                            "FROM FILMS WHERE NAME=?, ?)", updatedFilm.getName(), it.next().getId());
                }
            } else {
                jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID=?", updatedFilm.getId());
            }
        } catch (NullPointerException e) {
            log.info("У фильма {} не указаны жанры", updatedFilm.getName());
        }
        log.info("Данные о фильме № {} обновлены в БД", updatedFilm.getId());
        return updatedFilm;
    }

    @Override
    public boolean delete(String name) {
        boolean isFilmExist = findByName(name).isPresent();
        if (!isFilmExist) {
            log.info("Фильм '{}' не найден в БД", name);
            return false;
        } else {
            String sql = "DELETE FROM FILMS WHERE NAME=?;";
            jdbcTemplate.update(sql, name);
            log.info("Фильм '{}' успешно удален из БД", name);
            return true;
        }
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT FILMS.*, MPA.NAME AS MPA_NAME, FG.GENRE_ID AS GENRE_ID, G.NAME AS GENRE_NAME " +
                "FROM FILMS " +
                "LEFT OUTER JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRES FG on FILMS.FILM_ID = FG.FILM_ID " +
                "LEFT OUTER JOIN GENRE G on FG.GENRE_ID = G.GENRE_ID";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(utilMapper.dateFromSql(rs.getTimestamp("release_date")))
                .duration(utilMapper.longFromString(rs.getString("duration")))
                .build());
        if (!films.isEmpty()) {
            return films;
        } else {
            log.info("В БД Пользователи отсутствуют");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<Film> findById(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT FILMS.*, MPA.NAME AS MPA_NAME, MPA.MPA_ID AS MPA_ID " +
                "FROM FILMS " +
                "LEFT OUTER JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID " +
                "WHERE FILMS.FILM_ID =?", id);
        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getLong("film_id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(utilMapper.dateFromSql(filmRows.getTimestamp("release_date")))
                    .duration(utilMapper.longFromString(filmRows.getString("duration")))
                    .mpa(new Mpa(filmRows.getInt("MPA_ID"), filmRows.getString("MPA_NAME")))
                    .build();
            film.setGenres(genreStorage.findFilmsGenres(id));
            return Optional.of(film);
        } else {
            log.info("Пользователь в БД № {} не найден", id);
            return Optional.empty();
        }
    }

    @Override
    public List<Film> findPopulars(Integer count) {
        String sql = String.format("SELECT FILMS.FILM_ID AS FILM_ID, COUNT(LIKES.USER_ID) AS LIKES " +
                "FROM FILMS " +
                "LEFT OUTER JOIN LIKES ON FILMS.FILM_ID = LIKES.FILM_ID " +
                "GROUP BY FILMS.FILM_ID " +
                "ORDER BY LIKES DESC " +
                "LIMIT %d", count);
        return jdbcTemplate.query(sql, (rs, rowNum) -> findById(rs.getLong("film_id")).get());
    }

    private long findIdByName(String name) {
        String sql = String.format("SELECT FILM_ID FROM FILMS WHERE NAME='%s'", name);
        List<Long> ids = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("film_id"));
        return ids.get(0);
    }

    private Optional<Film> findByName(String name) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT FILMS.*, MPA.NAME AS MPA_NAME, MPA.MPA_ID AS MPA_ID," +
                "FG.GENRE_ID AS GENRE_ID, G.NAME AS GENRE_NAME " +
                "FROM FILMS " +
                "LEFT OUTER JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID " +
                "LEFT OUTER JOIN FILM_GENRES FG on FILMS.FILM_ID = FG.FILM_ID " +
                "LEFT OUTER JOIN GENRE G on FG.GENRE_ID = G.GENRE_ID " +
                "WHERE FILMS.NAME = ?;", name);
        if (filmRows.next()) {
            Film film = Film.builder()
                    .id(filmRows.getLong("film_id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .releaseDate(utilMapper.dateFromSql(filmRows.getTimestamp("release_date")))
                    .duration(utilMapper.longFromString(filmRows.getString("duration")))
                    .mpa(new Mpa(filmRows.getInt("MPA_ID"), filmRows.getString("MPA_NAME")))
                    .build();
            film.setGenres(genreStorage.findFilmsGenres(film.getId()));
            return Optional.of(film);
        } else {
            log.info("Фильм {} не найден в БД", name);
            return Optional.empty();
        }
    }
}
