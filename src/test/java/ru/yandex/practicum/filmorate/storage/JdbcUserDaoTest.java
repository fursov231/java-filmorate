package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "myapp.trx.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class JdbcUserDaoTest {
    @Autowired
    private UserStorage userStorage;

    @Test
    public void test1_shouldBeAdded() {
        userStorage.add(getUser1());

        Optional<User> user = userStorage.findById(1);

        assertThat(user)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "Peter")
                );
    }

    @Test
    public void test2_shouldBeUpdated() {
        userStorage.add(getUser1());

        Optional<User> oldUser = userStorage.findById(1);

        userStorage.update(User.builder()
                .id(1)
                .name("John")
                .email("peter@mail.com")
                .login("Peter")
                .birthday(LocalDate.of(1990, 1, 1))
                .build());
        Optional<User> updatedUser = userStorage.findById(1);

        assertThat(oldUser)
                .isPresent()
                .isNotEqualTo(updatedUser);
        assertThat(updatedUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "John"));
    }

    @Test
    public void test3_shouldBeDeleted() {
        User user = getUser1();
        userStorage.add(user);

        Optional<User> userInDb = userStorage.findById(1);
        userStorage.delete(userInDb.get());
        Optional<User> deletedUserInDb = userStorage.findById(1);

        assertThat(userInDb)
                .isPresent();
        assertThat(deletedUserInDb)
                .isEmpty();
    }

    @Test
    public void test4_shouldBeFoundAll() {
        userStorage.add(getUser1());
        userStorage.add(getUser2());

        List<User> allUsers = userStorage.findAll();
        assertThat(allUsers.size()).isEqualTo(2);
    }

    @Test
    public void test5_shouldBeFoundById() {
        userStorage.add(getUser1());

        Optional<User> foundUser = userStorage.findById(1);
        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", "Peter"));
    }

    @Test
    public void test6_shouldBeFoundByEmail() {
        userStorage.add(getUser1());

        Optional<User> foundUser = userStorage.findByEmail("peter@mail.com");
        assertThat(foundUser)
                .isPresent()
                .hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("name", "Peter"));
    }

    private User getUser1() {
        return User.builder()
                .id(1)
                .name("Peter")
                .email("peter@mail.com")
                .login("Peter")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    private User getUser2() {
        return User.builder()
                .id(2)
                .name("John")
                .email("john@mail.com")
                .login("John")
                .birthday(LocalDate.of(1980, 1, 1))
                .build();
    }
}




