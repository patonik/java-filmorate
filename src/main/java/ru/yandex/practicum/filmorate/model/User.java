package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.WithoutWhitespace;

import java.time.LocalDateTime;

@Data
public class User {
    private int id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @WithoutWhitespace
    private String login;
    private String name;
    @Past
    private LocalDateTime birthday;
}
