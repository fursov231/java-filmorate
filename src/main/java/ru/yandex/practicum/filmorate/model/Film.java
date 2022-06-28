package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class Film {
    @NonNull
    @Setter
    private long id;
    @NotBlank
    @NonNull
    private String name;

    @NotBlank
    @NonNull
    @Size(max = 200)
    private String description;

    @NonNull
    private LocalDate releaseDate;

    @NonNull
    private long duration;

    private long rate;

    final private Mpa mpa;

    @Singular
    @Setter
    private List<Genre> genres;
}

