package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addAsFriend(long id, long friendId);

    void confirmAddingAsFriend(long id, long friendId);

    List<User> findUsersFriends(long userId);

    List<User> findCommonUsersFriends(long userId, long friendId);

    void removeFromFriends(long id, long friendId);

    boolean findFriendRequest(long friendId, long id);
}
