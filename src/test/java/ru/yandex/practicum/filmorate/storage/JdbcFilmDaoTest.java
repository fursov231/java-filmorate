package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "myapp.trx.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcFilmDaoTest {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private LikeStorage likeStorage;

    @Test
    public void test1_shouldBeAdded() {
        Film newFilm = filmStorage.add(getFilm1());

        assertThat(Optional.of(newFilm))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Obi-Wan Kenobi")
                );
    }

    @Test
    public void test2_shouldBeUpdated() {
        filmStorage.add(getFilm1());

        List<Genre> newGenres = new ArrayList<>(new ArrayList<>(List.of(new Genre(2, "Драма"), new Genre(4, "Триллер"))));
        Film updatedFilm = filmStorage.update(Film.builder()
                .id(1)
                .name("Kenobi")
                .description("TV Mini Series")
                .releaseDate(LocalDate.of(2022, 5, 27))
                .duration(1000)
                .mpa(new Mpa(1, "G"))
                .genres(newGenres)
                .build());

        assertThat(Optional.of(updatedFilm))
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Kenobi")
                                .hasFieldOrPropertyWithValue("mpa", new Mpa(1, "G"))
                                .hasFieldOrPropertyWithValue("genres", newGenres)
                );
    }

    @Test
    public void test3_shouldBeDeleted() {
        filmStorage.add(getFilm1());

        filmStorage.delete("Obi-Wan Kenobi");

        assertThat(filmStorage.findById(1))
                .isEmpty();
    }

    @Test
    public void test4_shouldBeFoundAll() {
        filmStorage.add(getFilm1());
        filmStorage.add(getFilm2());

        List<Film> films = filmStorage.findAll();

        assertEquals(films.size(), 2);
    }

    @Test
    public void test5_shouldBeFoundById() {
        filmStorage.add(getFilm1());
        Optional<Film> filmOptional = filmStorage.findById(1);

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Obi-Wan Kenobi")
                );
    }

    @Test
    public void test6_shouldBeFoundPopular() {
        filmStorage.add(getFilm1());
        filmStorage.add(getFilm2());
        userStorage.add(User.builder()
                .id(1)
                .email("boris@mail.com")
                .name("Boris")
                .login("boris")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());
        userStorage.add(User.builder()
                .id(2)
                .email("rok@mail.com")
                .name("Rok")
                .login("rok")
                .birthday(LocalDate.of(1980, 1, 1))
                .build());

        Optional<User> user_1 = userStorage.findById(1);
        Optional<User> user_2 = userStorage.findById(2);
        Optional<Film> film_2 = filmStorage.findById(2);
        likeStorage.like(film_2.get().getId(), user_1.get().getId());
        likeStorage.like(film_2.get().getId(), user_2.get().getId());
        List<Film> list = filmStorage.findPopulars(10);

        assertEquals(list.get(0).getName(), film_2.get().getName());
    }

    private Film getFilm1() {
        return Film.builder()
                .name("Obi-Wan Kenobi")
                .description("TV Mini Series")
                .releaseDate(LocalDate.of(2022, 5, 27))
                .duration(1000)
                .mpa(new Mpa(2, "PG"))
                .build();
    }

    private Film getFilm2() {
        return Film.builder()
                .name("The Boys")
                .description("TV Mini Series")
                .releaseDate(LocalDate.of(2019, 7, 17))
                .duration(1000)
                .mpa(new Mpa(2, "PG"))
                .build();
    }
}


