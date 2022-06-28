package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void like(long filmId, long userId);

    void unlike(long filmId, long userId);

    boolean isLikeByUser(long filmId, long userId);
}
