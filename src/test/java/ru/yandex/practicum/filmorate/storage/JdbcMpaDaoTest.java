package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "myapp.trx.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcMpaDaoTest {
    @Autowired
    private MpaStorage mpaStorage;


    @Test
    public void test9_shouldBeFoundMpaById() {
        Mpa mpa = new Mpa(2, "PG");

        Mpa targetMpa = mpaStorage.findMpaById(2);

        assertThat(mpa)
                .isEqualTo(targetMpa);
    }

    @Test
    public void test10_shouldBeFoundAllMpa() {
        List<Mpa> mpas = mpaStorage.findAllMpa();

        assertThat(mpas.size())
                .isEqualTo(5);
    }
}
