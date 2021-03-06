package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class User {
    @NonNull
    private long id;

    @Email
    @NotBlank
    @NonNull
    private String email;

    @NotBlank
    @NonNull
    private String login;

    @NonNull
    @Setter
    private String name;

    @NonNull
    private LocalDate birthday;

    @Setter
    private List<User> friends;
}

