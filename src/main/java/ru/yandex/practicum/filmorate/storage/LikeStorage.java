package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    boolean like(long filmId, long userId);

    boolean unlike(long filmId, long userId);


}
