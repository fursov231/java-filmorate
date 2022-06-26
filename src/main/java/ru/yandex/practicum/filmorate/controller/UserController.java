package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {
    final private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Optional<User> add(@RequestBody @Valid User newUser) {
        return userService.add(newUser);
       // JsonMapper.getInstance().writeValueAsString(newUser);
    }

    @PutMapping
    public User update(@RequestBody @Valid User updatedUser) {
        return userService.update(updatedUser);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable long id) {
            return userService.findById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addAsFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addAsFriend(id, friendId);
    }

    @PutMapping("/{id}/confirm/{friendId}")
    public ResponseEntity confirmAddingAsFriend(@PathVariable long id, @PathVariable long friendId) {
        if (userService.confirmAddingAsFriend(id, friendId)) {
            return new ResponseEntity("Заявка подтверждена");
        }
        return new ResponseEntity("Запрос на подтверждение дружбы не выполнен");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable long id) {
        return userService.findUsersFriends(id);
    }

   @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> findCommonUsersFriends(@PathVariable long userId, @PathVariable long friendId) {
        return userService.findCommonUsersFriends(userId, friendId);
    }
}
