package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolationException;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    private FilmController filmController;

    @Test
    void shouldBeAddedWithoutEmptyName() {
        Film film = new Film("Syndicate", "Description",
                LocalDate.of(2001, 12, 12), 100, 5);
        Film response = filmController.add(film);

        Assertions.assertEquals(response, film);
    }

    @Test
    void shouldNotBeAddedWithEmptyName() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> filmController.add(new Film("", "Description",
                        LocalDate.of(2001, 12, 12), 100, 5)));

        Assertions.assertEquals(exception.getMessage(), "add.newFilm.name: не должно быть пустым");
    }

    @Test
    void shouldBeUpdatedWithoutEmptyName() {
        Film film = new Film("Syndicate", "Description",
                LocalDate.of(2001, 12, 12), 100, 5);
        Film response = filmController.add(film);

        Film updatedFilm = new Film("new Syndicate", "Description",
                LocalDate.of(2001, 12, 12), 100, 5);
        updatedFilm.setId(response.getId());
        Film updateResponse = filmController.update(updatedFilm);

        Assertions.assertEquals(updateResponse, updatedFilm);
    }

    @Test
    void shouldNotBeUpdatedWithEmptyName() {
        Film film = new Film("Syndicate", "Description",
                LocalDate.of(2001, 12, 12), 100, 5);
        filmController.add(film);

        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> filmController.add(new Film("", "Description",
                        LocalDate.of(2001, 12, 12), 100, 5)));

        Assertions.assertEquals(exception.getMessage(), "add.newFilm.name: не должно быть пустым");
    }

    @Test
    void shouldBeAddedWithDescriptionLengthLessThan200chars() {
        Film film = new Film("Syndicate", "Профессиональный военный Джексон Бриггс всеми " +
                "силами пытается вернуться в строй, но из-за травмы головы получает постоянные отказы. Когда умирает " +
                "один из его сослуживцев, Бриггсу дают задание: с воен",
                LocalDate.of(2001, 12, 12), 100, 5);
       Film response = filmController.add(film);

        Assertions.assertEquals(response, film);
    }

    @Test
    void shouldNotBeAddedWithDescriptionLengthMoreThan200chars() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> filmController.add(new Film("Syndicate", "Профессиональный военный Джексон " +
                        "Бриггс всеми силами пытается вернуться в строй, но из-за травмы головы получает постоянные " +
                        "отказы. Когда умирает один из его сослуживцев, Бриггсу дают задание: с военной",
                        LocalDate.of(2001, 12, 12), 100, 5)));

        Assertions.assertEquals(exception.getMessage(), "add.newFilm.description: размер должен находиться в диапазоне от 0 до 200");
    }

    @Test
    void shouldNotBeAddedWithEmptyDescription() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> filmController.add(new Film("Syndicate", "",
                        LocalDate.of(2001, 12, 12), 100, 5)));

        Assertions.assertEquals(exception.getMessage(), "add.newFilm.description: не должно быть пустым");
    }

    @Test
    void shouldBeAddedWithReleaseDateAfter1895_12_28() {
        Film film = new Film("Syndicate", "Description",
                LocalDate.of(1895, 12, 29), 100, 5);
        Film response = filmController.add(film);

        Assertions.assertEquals(response, film);
    }

    @Test
    void shouldNotBeAddedWithReleaseDateBefore1895() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.add(new Film("Syndicate", "Description",
                        LocalDate.of(1895, 12, 27), 100, 5)));

        Assertions.assertEquals(exception.getMessage(), "Введен неверный год выпуска");
    }

    @Test
    void shouldBeAddedWithPositiveDuration() {
        Film film = new Film("Syndicate", "Description",
                LocalDate.of(1895, 12, 29), 100, 5);
       Film response = filmController.add(film);

        Assertions.assertEquals(response, film);

    }

    @Test
    void shouldNotBeAddedWithNegativeDuration() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.add(new Film("Syndicate", "Description",
                        LocalDate.of(1895, 12, 27), 100, 5)));

        Assertions.assertEquals(exception.getMessage(), "Введена неверная продолжительность фильма");
    }
}


