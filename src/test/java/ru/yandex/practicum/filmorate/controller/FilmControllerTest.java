package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        Film film = new Film(1, "Syndicate", "",
                LocalDate.of(2001, 12, 12), Duration.ofHours(2));
        filmController.add(film);
        Film savedFilm = filmController.getMap().get(1);

        Assertions.assertEquals(film, savedFilm);
        Assertions.assertEquals(filmController.getMap().size(), 1);
    }

    @Test
    void shouldNotBeAddedWithEmptyName() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> filmController.add(new Film(1, "", "",
                        LocalDate.of(2001, 12, 12), Duration.ofHours(2))));

        Assertions.assertEquals(exception.getMessage(), "add.newFilm.name: не должно быть пустым");
    }

    @Test
    void shouldBeUpdatedWithoutEmptyName() {
        Film film = new Film(1, "Syndicate", "",
                LocalDate.of(2001, 12, 12), Duration.ofHours(2));
        filmController.add(film);

        Film updatedFilm = new Film(1, "new Syndicate", "",
                LocalDate.of(2001, 12, 12), Duration.ofHours(2));
        filmController.update(updatedFilm);

        Film savedFilm = filmController.getMap().get(1);

        Assertions.assertEquals(updatedFilm, savedFilm);
        Assertions.assertEquals(filmController.getMap().size(), 1);
    }

    @Test
    void shouldNotBeUpdatedWithEmptyName() {
        Film film = new Film(1, "Syndicate", "",
                LocalDate.of(2001, 12, 12), Duration.ofHours(2));
        filmController.add(film);

        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> filmController.add(new Film(1, "", "",
                        LocalDate.of(2001, 12, 12), Duration.ofHours(2))));

        Assertions.assertEquals(exception.getMessage(), "add.newFilm.name: не должно быть пустым");
    }

    @Test
    void shouldBeAddedWithDescriptionLengthLessThan200chars() {
        Film film = new Film(1, "Syndicate", "Профессиональный военный Джексон Бриггс всеми " +
                "силами пытается вернуться в строй, но из-за травмы головы получает постоянные отказы. Когда умирает " +
                "один из его сослуживцев, Бриггсу дают задание: с воен",
                LocalDate.of(2001, 12, 12), Duration.ofHours(2));
        filmController.add(film);
        Film savedFilm = filmController.getMap().get(1);

        Assertions.assertEquals(film, savedFilm);
        Assertions.assertEquals(filmController.getMap().size(), 1);
    }

    @Test
    void shouldNotBeAddedWithDescriptionLengthMoreThan200chars() {
        final ConstraintViolationException exception = assertThrows(
                ConstraintViolationException.class,
                () -> filmController.add(new Film(1, "Syndicate", "Профессиональный военный Джексон " +
                        "Бриггс всеми силами пытается вернуться в строй, но из-за травмы головы получает постоянные " +
                        "отказы. Когда умирает один из его сослуживцев, Бриггсу дают задание: с военной",
                        LocalDate.of(2001, 12, 12), Duration.ofHours(2))));

        Assertions.assertEquals(exception.getMessage(), "add.newFilm.description: размер должен находиться в диапазоне от 0 до 200");
    }

    @Test
    void shouldBeAddedWithReleaseDateAfter1895_12_28() {
        Film film = new Film(1, "Syndicate", "",
                LocalDate.of(1895, 12, 29), Duration.ofHours(2));
        filmController.add(film);
        Film savedFilm = filmController.getMap().get(1);

        Assertions.assertEquals(film, savedFilm);
        Assertions.assertEquals(filmController.getMap().size(), 1);
    }

    @Test
    void shouldNotBeAddedWithReleaseDateBefore1895() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.add(new Film(1, "Syndicate", "",
                        LocalDate.of(1895, 12, 27), Duration.ofHours(2))));

        Assertions.assertEquals(exception.getMessage(), "Введен неверный год выпуска");
    }

    @Test
    void shouldBeAddedWithPositiveDuration() {
        Film film = new Film(1, "Syndicate", "",
                LocalDate.of(1895, 12, 29), Duration.ofSeconds(1));
        filmController.add(film);
        Film savedFilm = filmController.getMap().get(1);

        Assertions.assertEquals(film, savedFilm);
        Assertions.assertEquals(filmController.getMap().size(), 1);
    }

    @Test
    void shouldNotBeAddedWithNegativeDuration() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.add(new Film(1, "Syndicate", "",
                        LocalDate.of(1895, 12, 27), Duration.ofSeconds(-1))));

        Assertions.assertEquals(exception.getMessage(), "Введена неверная продолжительность фильма");
    }

}

/*
название не может быть пустым;
максимальная длина описания — 200 символов;
дата релиза — не раньше 28 декабря 1895 года;
продолжительность фильма должна быть положительной.
*/
