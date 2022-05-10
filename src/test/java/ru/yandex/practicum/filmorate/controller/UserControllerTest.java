package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;

    @Test
    void shouldBeAddedWithFilledAndValidEmail() {
        User user = new User(1, "user@mail.com", "user", "Ivan",
                LocalDate.of(1980, 1, 1));
        userController.add(user);
        User savedUser = userController.getMap().get(1);

        Assertions.assertEquals(user, savedUser);
        Assertions.assertEquals(userController.getMap().size(), 1);
    }

    @Test
    void shouldNotBeAddedWithUnfilledEmail() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> userController.add(new User(1, "", "user", "Ivan",
                        LocalDate.of(1980, 1, 1))));

        Assertions.assertEquals(exception.getMessage(), "add.newUser.email: не должно быть пустым");
    }

    @Test
    void shouldNotBeAddedWithoutAtInEmail() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> userController.add(new User(1, "user.com", "user", "Ivan",
                        LocalDate.of(1980, 1, 1))));

        Assertions.assertEquals(exception.getMessage(), "add.newUser.email: должно иметь формат адреса электронной почты");
    }

    @Test
    void shouldBeAddedWithFilledLoginWithoutSpaces() {
        User user = new User(1, "user@mail.com", "user", "Ivan",
                LocalDate.of(1980, 1, 1));
        userController.add(user);
        User savedUser = userController.getMap().get(1);

        Assertions.assertEquals(user, savedUser);
        Assertions.assertEquals(userController.getMap().size(), 1);
    }

    @Test
    void shouldNotBeAddedWithEmptyLogin() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> userController.add(new User(1, "user@mail.com", "", "Ivan",
                        LocalDate.of(1980, 1, 1))));

        Assertions.assertEquals(exception.getMessage(), "add.newUser.login: не должно быть пустым");
    }

    @Test
    void shouldNotBeAddedWithSpacesInLogin() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.add(new User(1, "user@mail.com", "user #1", "Ivan",
                        LocalDate.of(1980, 1, 1))));

        Assertions.assertEquals(exception.getMessage(), "Логин не должен содержать пробелы");
    }

    @Test
    void shouldBeAddedLoginAsNameIfNameIsEmpty() {
        User user = new User(1, "user@mail.com", "user", "",
                LocalDate.of(1980, 1, 1));
        userController.add(user);
        String savedName = userController.getMap().get(1).getName();
        String login = userController.getMap().get(1).getLogin();

        Assertions.assertEquals(login, savedName);
    }

    @Test
    void shouldNotBeAddedIfBirthdayInFuture() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userController.add(new User(1, "user@mail.com", "user", "Ivan",
                        LocalDate.of(2030, 1, 1))));

        Assertions.assertEquals(exception.getMessage(), "Дата рождения не может быть в будущем");
    }
}