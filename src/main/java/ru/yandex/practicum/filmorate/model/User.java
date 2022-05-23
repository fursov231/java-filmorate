package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class User {
    private long id;

    @Email
    @NotBlank
    @NonNull
    private String email;

    @NotBlank
    @NonNull
    private String login;

    @NonNull
    private String name;

    @NonNull
    private LocalDate birthday;

    private Set<Long> friendsIdSet = new HashSet<>();

    public void addAsFriend(long id) {
        friendsIdSet.add(id);
    }

    public void removeFromFriends(long id) {
        friendsIdSet.remove(id);
    }

    public Set<Long> getFriends() {
        return friendsIdSet;
    }
}

