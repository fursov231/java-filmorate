package ru.yandex.practicum.filmorate.storage.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.UtilMapper;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcUserDao implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(JdbcUserDao.class);
    private final JdbcTemplate jdbcTemplate;
    private final UtilMapper utilMapper;
    private final FriendStorage friendStorage;

    public JdbcUserDao(JdbcTemplate jdbcTemplate, UtilMapper utilMapper, FriendStorage friendStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilMapper = utilMapper;
        this.friendStorage = friendStorage;
    }

    @Override
    public void add(User newUser) {
        String sql = "INSERT INTO USERS (NAME, EMAIL, LOGIN, BIRTHDAY) VALUES (?, ?, ?, ?);";
        jdbcTemplate.update(sql, newUser.getName(), newUser.getEmail(), newUser.getLogin(),
                Timestamp.valueOf(newUser.getBirthday().atStartOfDay()));
        log.info("Пользователь {} добавлен в БД", newUser.getEmail());
    }

    @Override
    public void update(User updatedUser) {
        String sql = "UPDATE USERS SET NAME=?, EMAIL=?, LOGIN=?, BIRTHDAY=? WHERE USER_ID=?";
        jdbcTemplate.update(sql, updatedUser.getName(), updatedUser.getEmail(), updatedUser.getLogin(),
                Timestamp.valueOf(updatedUser.getBirthday().atStartOfDay()), updatedUser.getId());
        log.info("Данные о пользователе {} - {} обновлены в БД", updatedUser.getId(), updatedUser.getName());
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM USERS WHERE EMAIL=?;";
        jdbcTemplate.update(sql, user.getEmail());
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM USERS;";
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(utilMapper.dateFromSql(rs.getTimestamp("birthday")))
                .build()
        );
        if (!users.isEmpty()) {
            log.info("В БД найдены пользователи: {}", users);
            return users;
        } else {
            log.info("В БД Пользователи отсутствуют");
            return Collections.emptyList();
        }
    }

    @Override
    public Optional<User> findById(long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * " +
                "FROM USERS " +
                "WHERE USER_ID =?", id);
        if (userRows.next()) {
            User user = User.builder()
                    .id(userRows.getLong("user_id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(utilMapper.dateFromSql(userRows.getTimestamp("birthday")))
                    .build();
            user.setFriends(friendStorage.findUsersFriends(id));
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM USERS WHERE EMAIL = ?;", email);
        if (userRows.next()) {
            User user = User.builder()
                    .id(userRows.getLong("user_id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(utilMapper.dateFromSql(userRows.getTimestamp("birthday")))
                    .build();
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }
}
