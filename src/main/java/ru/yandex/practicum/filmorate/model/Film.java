package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.IsAfter;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @IsAfter(message = "the date should be later than 28.12.1985", checkDate = "1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private long duration;
}
