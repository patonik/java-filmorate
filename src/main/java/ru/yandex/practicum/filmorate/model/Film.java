package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.IsAfter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    @Min(value = Integer.MIN_VALUE, message = "id out of range")
    @Max(value = Integer.MAX_VALUE, message = "id out of range")
    private int id;
    @NotBlank(message = "name cannot be blank")
    private String name;
    @Size(max = 200, message = "description must be within 200 chars limit")
    private String description;
    @IsAfter(message = "the date should be later than 28.12.1895", checkDate = "1895-12-28")
    private LocalDate releaseDate;
    @Positive(message = "duration should be positive number")
    private long duration;
    private final Set<User> usersLiked = new HashSet<>();
}
