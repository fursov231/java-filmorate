package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film  {
    private long id;

    @NotBlank
    @NonNull
    final private String name;

    @NotBlank
    @NonNull
    @Size(max = 200)
    final private String description;

    @NonNull
    final private LocalDate releaseDate;

    @NonNull
    final private long duration;

    final private long rate;

    final private String genre;

    final private String mpa;

    private Set<Long> likesId = new HashSet<>();

    public void like(long id) {
        likesId.add(id);
    }

    public void unlike(long id) {
        likesId.remove(id);
    }

    public Set<Long> getLikes() {
        return likesId;
    }

    public long getLikesCount() {
        return getLikes().size();
    }
}

