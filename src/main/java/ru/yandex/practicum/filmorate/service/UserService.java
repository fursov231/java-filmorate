package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addAsFriend(long id, long friendId) {
        User user = userStorage.findById(id);
        user.addAsFriend(friendId);
        User otherUser = userStorage.findById(friendId);
        otherUser.addAsFriend(id);
        userStorage.update(user);
        userStorage.update(otherUser);
    }

    public void removeFriend(User user, User targetUser) {
        user.removeFromFriends(targetUser.getId());
        targetUser.removeFromFriends(user.getId());
        userStorage.update(user);
        userStorage.update(targetUser);
    }

    public List<User> findUserFriends(long id) {
        List<User> userList = new ArrayList<>();
        Set<Long> friendsSet = userStorage.findById(id).getFriends();
        friendsSet.forEach(e -> userList.add(userStorage.findById(e)));
        return userList;
    }

    public List<User> showMutualUserFriends(long id, long otherId) {
        List<User> userMutualFriends = new ArrayList<>();
        Set<Long> userFriendsIdSet = userStorage.findById(id).getFriends();
        Set<Long> otherUserFriendsIdSet = userStorage.findById(otherId).getFriends();
        for (var uid : userFriendsIdSet) {
            if (otherUserFriendsIdSet.contains(uid)) {
                userMutualFriends.add(userStorage.findById(uid));
            }
        }
        return userMutualFriends;
    }
}
