package ru.yandex.practicum.filmorate.storage.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Component
public class JdbcMpaDao implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public JdbcMpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa findMpaById(long id) {
        String sql = String.format("SELECT MPA_ID, NAME " +
                "FROM MPA " +
                "WHERE MPA_ID=%d", id);
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"), rs.getString("name"))).get(0);
    }

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "SELECT * FROM MPA";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Mpa(rs.getInt("mpa_id"), rs.getString("name")));
    }
}
