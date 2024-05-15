package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.IsAfter;

import java.time.LocalDateTime;

/**
 * Film.
 */
@Data
public class Film {
    private int id;
    @NotBlank
    @Size(max = 200)
    private String name;
    private String description;
    @IsAfter(message = "the date should be later than 28.12.1985", valid = "1985-12-28T00:00:00")
    private LocalDateTime releaseDate;
    @Positive
    private long duration;
}
