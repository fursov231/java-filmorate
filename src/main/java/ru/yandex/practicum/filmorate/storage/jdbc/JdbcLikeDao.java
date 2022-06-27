package ru.yandex.practicum.filmorate.storage.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Component
public class JdbcLikeDao implements LikeStorage {
    private final Logger log = LoggerFactory.getLogger(JdbcUserDao.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcLikeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean like(long filmId, long userId) {
        if (isLikeByUser(userId, filmId)) {
            log.info("Не удалось добавить лайк пользователя № {} фильму {} в БД", userId, filmId);
            return false;
        } else {
            String sql = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES(?, ?)";
            jdbcTemplate.update(sql, filmId, userId);
            log.info("Лайк пользователя № {} фильму {} добавлен в БД", userId, filmId);
            return true;
        }
    }

    @Override
    public boolean unlike(long filmId, long userId) {
        if (isLikeByUser(filmId, userId)) {
            String sql = "DELETE FROM LIKES WHERE (FILM_ID=? AND USER_ID=?)";
            jdbcTemplate.update(sql, filmId, userId);
            log.info("Лайк пользователя № {} фильму {} отменен в БД", userId, filmId);
            return true;
        } else {
            log.info("Не удалось отменить лайк пользователя № {} фильму {} в БД", userId, filmId);
            return false;
        }
    }

    private boolean isLikeByUser(long filmId, long userId) {
        String sql = String.format("SELECT COUNT(USER_ID) AS LIKES " +
                "FROM LIKES " +
                "WHERE USER_ID=%d AND FILM_ID=%d " +
                "GROUP BY FILM_ID", userId, filmId);
        List<Integer> likes = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("LIKES"));
        return !likes.isEmpty();
    }
}
