package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class User {
    private int id;

    @Email
    @NotBlank
    final private String email;

    @NotBlank
    final private String login;

    @NonNull
    String name;

    final private LocalDate birthday;
}

