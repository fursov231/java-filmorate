package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "myapp.trx.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcLikeDaoTest {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private LikeStorage likeStorage;

    @Test
    public void test1_shouldBeLike() {
        filmStorage.add(getFilm1());
        userStorage.add(User.builder()
                .id(1)
                .email("boris@mail.com")
                .name("Boris")
                .login("boris")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());

        Optional<User> user = userStorage.findById(1);
        Optional<Film> film = filmStorage.findById(1);

        likeStorage.like(film.get().getId(), user.get().getId());
        assertTrue(likeStorage.isLikeByUser(film.get().getId(), user.get().getId()));
    }

    @Test
    public void test2_shouldBeUnlike() {
        filmStorage.add(getFilm1());
        userStorage.add(User.builder()
                .id(1)
                .email("boris@mail.com")
                .name("Boris")
                .login("boris")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());

        Optional<User> user = userStorage.findById(1);
        Optional<Film> film = filmStorage.findById(1);
        likeStorage.like(film.get().getId(), user.get().getId());
        likeStorage.unlike(film.get().getId(), user.get().getId());

        assertFalse(likeStorage.isLikeByUser(film.get().getId(), user.get().getId()));
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
}
