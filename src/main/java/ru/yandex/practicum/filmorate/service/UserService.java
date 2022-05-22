package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private int id;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User newUser) {
        if (newUser.getName().isEmpty()) {
            log.info("Имя присвоено по названию логина");
            newUser.setName(newUser.getLogin());
        }
        if (newUser.getLogin().chars().anyMatch(Character::isWhitespace)) {
            log.info("Введен логин с пробелом");
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.info("Введен неверный год рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        newUser.setId(++id);
        userStorage.add(newUser);
        log.info("Запрос на добавление пользователя выполнен");
        return newUser;
    }

    public User update(User updatedUser) {
        if (userStorage.findById(updatedUser.getId()) == null) {
            throw new NotFoundException("Передан неверный id");
        }
        userStorage.update(updatedUser);
        log.info("Запрос на обновление пользователя выполнен");
        return updatedUser;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long id) {
        if (userStorage.findById(id) == null) {
            throw new NotFoundException("Передан неверный id");
        }
        return userStorage.findById(id);
    }

    public void addAsFriend(long id, long friendId) {
        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
        User user = userStorage.findById(id);
        user.addAsFriend(friendId);
        User otherUser = userStorage.findById(friendId);
        otherUser.addAsFriend(id);
        userStorage.update(user);
        userStorage.update(otherUser);
        log.info("Запрос на добавление в друзья выполнен");
    }

    public void removeFriend(long id, long friendId) {
        if (userStorage.findById(id) == null || userStorage.findById(friendId) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
        User user = findById(id);
        User friend = findById(friendId);
        user.removeFromFriends(friendId);
        friend.removeFromFriends(id);
        userStorage.update(user);
        userStorage.update(friend);
        log.info("Запрос на удаление из друзей выполнен");
    }

    public List<User> findUserFriends(long id) {
        if (userStorage.findById(id) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
        List<User> userList = new ArrayList<>();
        Set<Long> friendsSet = userStorage.findById(id).getFriends();
        friendsSet.forEach(e -> userList.add(userStorage.findById(e)));
        return userList;
    }

    public List<User> showMutualUserFriends(long id, long otherId) {
        if (userStorage.findById(id) == null || userStorage.findById(otherId) == null) {
            throw new NotFoundException("Ошибка в передаче id");
        }
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
