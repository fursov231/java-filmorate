package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film  {
    private int id;

    @NotBlank
    final private String name;

    @NotBlank
    @Size(max = 200)
    final private String description;

    final private LocalDate releaseDate;
    final private Duration duration;
}

