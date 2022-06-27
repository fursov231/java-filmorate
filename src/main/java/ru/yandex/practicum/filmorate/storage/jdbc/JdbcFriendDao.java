package ru.yandex.practicum.filmorate.storage.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.UtilMapper;

import java.util.Collections;
import java.util.List;

@Component
public class JdbcFriendDao implements FriendStorage {
    private final Logger log = LoggerFactory.getLogger(JdbcUserDao.class);
    private final JdbcTemplate jdbcTemplate;
    private final UtilMapper utilMapper;
    private final UserStorage userStorage;

    public JdbcFriendDao(JdbcTemplate jdbcTemplate, UtilMapper utilMapper, UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.utilMapper = utilMapper;
        this.userStorage = userStorage;
    }

    @Override
    public void addAsFriend(long id, long friendId) {
        String sql = "INSERT INTO FRIENDS (OUTGOING_USER_ID, INCOMING_USER_ID, STATUS) " +
                "VALUES (?, ?, 'APPROVED');";
        jdbcTemplate.update(sql, id, friendId);
        log.info("Запрос к БД на добавление в друзья отправлен");
    }

    @Override
    public void confirmAddingAsFriend(long id, long friendId) {
        String sql = "UPDATE FRIENDS SET STATUS = 'APPROVED' WHERE INCOMING_USER_ID = ?  AND OUTGOING_USER_ID = ?";
        jdbcTemplate.update(sql, id, friendId);
        log.info("Запрос к БД на подтверждение дружбы выполнен");
    }

    @Override
    public List<User> findUsersFriends(long userId) {
        String sql = String.format("SELECT INCOMING_USER_ID AS USER_ID, U.NAME, U.EMAIL, U.LOGIN, U.BIRTHDAY " +
                "FROM FRIENDS " +
                "JOIN USERS U ON U.USER_ID = FRIENDS.INCOMING_USER_ID " +
                "WHERE OUTGOING_USER_ID=%d AND STATUS='APPROVED';", userId);
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> User.builder()
                .id(rs.getLong("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(utilMapper.dateFromSql(rs.getTimestamp("birthday")))
                .build()
        );
        if (!users.isEmpty()) {
            return users;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<User> findCommonUsersFriends(long userId, long friendId) {
        String sql = String.format("SELECT F1.INCOMING_USER_ID AS USER_ID " +
                "FROM friends AS F1 " +
                "JOIN friends AS F2 ON F1.INCOMING_USER_ID = F2.INCOMING_USER_ID AND F2.OUTGOING_USER_ID=%d " +
                "WHERE F1.OUTGOING_USER_ID=%d;", userId, friendId);
        List<User> users = jdbcTemplate.query(sql, (rs, rowNum) -> userStorage.findById(rs.getInt("user_id")).get());
        if (!users.isEmpty()) {
            return users;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void removeFromFriends(long id, long friendId) {
        String sql = "DELETE FROM FRIENDS WHERE OUTGOING_USER_ID=? AND INCOMING_USER_ID=?" +
                "OR INCOMING_USER_ID=? AND OUTGOING_USER_ID=?;";
        jdbcTemplate.update(sql, id, friendId, friendId, id);
        log.info("Запрос к БД на удаление из друзей выполнен");
    }

    @Override
    public boolean findFriendRequest(long friendId, long id) {
        String sql = String.format("SELECT STATUS FROM FRIENDS WHERE OUTGOING_USER_ID=%s AND INCOMING_USER_ID=%s;", friendId, id);
        List<String> status = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("status"));
        if (status.contains("UNAPPROVED")) {
            return true;
        } else {
            return false;
        }
    }
}
