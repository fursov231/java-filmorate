package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "myapp.trx.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcGenreDaoTest {
    @Autowired
    private GenreStorage genreStorage;

    @Test
    public void test11_shouldBeFoundGenreById() {
        Genre genre = new Genre(2, "Драма");

        Genre targetGenre = genreStorage.findGenreById(2);

        assertThat(genre)
                .isEqualTo(targetGenre);
    }

    @Test
    public void test12_shouldBeFoundAllGenres() {
        List<Genre> genres = genreStorage.findAllGenres();

        assertThat(genres.size())
                .isEqualTo(6);
    }
}
