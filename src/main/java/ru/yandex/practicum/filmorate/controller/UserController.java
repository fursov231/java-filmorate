package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.RecommendationService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {
    final private UserService userService;
    final private RecommendationService recommendationService;

    @Autowired
    public UserController(UserService userService, RecommendationService recommendationService) {
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public Optional<User> add(@RequestBody @Valid User newUser) {
        return userService.add(newUser);
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
    public ResponseEntity<String> addAsFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addAsFriend(id, friendId);
        return new ResponseEntity<>("Друг добавлен", HttpStatus.OK);
    }

    @PutMapping("/{id}/confirm/{friendId}")
    public ResponseEntity<String> confirmAddingAsFriend(@PathVariable long id, @PathVariable long friendId) {
        if (userService.confirmAddingAsFriend(id, friendId)) {
            return new ResponseEntity<>("Заявка подтверждена", HttpStatus.OK);
        }
        return new ResponseEntity<>("Запрос на подтверждение дружбы не выполнен", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> removeFromFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriend(id, friendId);
        return new ResponseEntity<>("Друг удален", HttpStatus.OK);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable long id) {
        return userService.findUsersFriends(id);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> findCommonUsersFriends(@PathVariable long userId, @PathVariable long friendId) {
        return userService.findCommonUsersFriends(userId, friendId);
    }

    @GetMapping("/users/{id}/recommendations")
    ResponseEntity<?> findRecommendationsById(@PathVariable int id) {
        return ResponseEntity.ok(recommendationService.findRecommendedFilms(id));
    }
}
