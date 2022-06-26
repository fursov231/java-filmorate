package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    void add(User newUser);

    void update(User updatedUser);

    void delete(User user);

    List<User> findAll();

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    void addAsFriend(long id, long friendId);

    void confirmAddingAsFriend(long id, long friendId);

    List<User> findUsersFriends(long id);

    void removeFromFriends(long id, long friendId);

    boolean findFriendRequest(long id, long friendId);

    List<User> findCommonUsersFriends (long userId, long friendId);
}
