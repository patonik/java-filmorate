package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.WithoutWhitespace;

import java.time.LocalDate;

@Data
public class User {
    @NotNull
    @Min(Integer.MIN_VALUE)
    @Max(Integer.MAX_VALUE)
    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @WithoutWhitespace
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}
