package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private int id;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String login;

    String name;
    private LocalDate birthday;
}

