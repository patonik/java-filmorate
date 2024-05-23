package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.WithoutWhitespace;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @Min(value = Integer.MIN_VALUE, message = "id out of range")
    @Max(value = Integer.MAX_VALUE, message = "id out of range")
    private int id;
    @Email(message = "email incorrect")
    @NotBlank(message = "email cannot be blank")
    private String email;
    @NotBlank(message = "login cannot be blank")
    @WithoutWhitespace
    private String login;
    private String name;
    @Past(message = "birthday should be in the past")
    private LocalDate birthday;
    private final Set<User> friends = new HashSet<>();
}
