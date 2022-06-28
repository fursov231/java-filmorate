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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"spring.config.name=myapp-test-h2", "myapp.trx.datasource.url=jdbc:h2:mem:trxServiceStatus"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class JdbcFriendDaoTest {
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private FriendStorage friendStorage;

    @Test
    public void test1_shouldBeAddedAsFriendAndFoundIt() {
        userStorage.add(getUser1());
        User targetUser = getUser2();
        userStorage.add(targetUser);

        User user_1 = userStorage.findById(1).get();
        User user_2 = userStorage.findById(2).get();
        friendStorage.addAsFriend(user_1.getId(), user_2.getId());
        User friendOfUser_1 = friendStorage.findUsersFriends(user_1.getId()).get(0);

        assertThat(friendOfUser_1.getEmail())
                .isEqualTo(targetUser.getEmail());
    }

    @Test
    public void test2_shouldBeConfirmedAddingAsFriendAndFoundIt() {
        userStorage.add(getUser1());
        User targetUser = getUser2();
        userStorage.add(targetUser);

        User user_1 = userStorage.findById(1).get();
        User user_2 = userStorage.findById(2).get();
        friendStorage.addAsFriend(user_1.getId(), user_2.getId());
        friendStorage.confirmAddingAsFriend(user_2.getId(), user_1.getId());
        User friendOfUser_1 = friendStorage.findUsersFriends(user_1.getId()).get(0);

        assertThat(friendOfUser_1.getEmail())
                .isEqualTo(targetUser.getEmail());
    }


    @Test
    public void test3_shouldBeRemovedFromFriends() {
        userStorage.add(getUser1());
        User targetUser = getUser2();
        userStorage.add(targetUser);

        User user_1 = userStorage.findById(1).get();
        User user_2 = userStorage.findById(2).get();
        friendStorage.addAsFriend(user_1.getId(), user_2.getId());
        friendStorage.confirmAddingAsFriend(user_2.getId(), user_1.getId());
        friendStorage.removeFromFriends(user_1.getId(), user_2.getId());
        List<User> friendOfUser_1 = friendStorage.findUsersFriends(user_1.getId());

        assertThat(friendOfUser_1)
                .isEmpty();
    }

    @Test
    public void test4_shouldBeFoundCommonUsersFriends() {
        userStorage.add(getUser1());
        User targetUser_2 = getUser2();
        userStorage.add(targetUser_2);
        userStorage.add(User.builder()
                .id(3)
                .name("Will")
                .email("will@mail.com")
                .login("Will")
                .birthday(LocalDate.of(1970, 1, 1))
                .build());

        User user_1 = userStorage.findById(1).get();
        User user_2 = userStorage.findById(2).get();
        User user_3 = userStorage.findById(3).get();
        friendStorage.addAsFriend(user_1.getId(), user_2.getId());
        friendStorage.addAsFriend(user_3.getId(), user_2.getId());
        User mutualFriendOfUser1And3 = friendStorage.findCommonUsersFriends(user_1.getId(), user_3.getId()).get(0);

        assertThat(mutualFriendOfUser1And3.getEmail())
                .isEqualTo(targetUser_2.getEmail());
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
