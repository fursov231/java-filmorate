package ru.yandex.practicum.filmorate.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class UtilMapper {
    public LocalDate dateFromSql(Timestamp timestamp) {
        return timestamp.toLocalDateTime().toLocalDate();
    }

    public Long longFromString(String duration) {
        return Long.valueOf(duration);
    }
}
