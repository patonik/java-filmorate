package ru.yandex.practicum.filmorate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Configuration
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate"})
public class ServiceConfig {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public ServiceConfig(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Bean
    public FilmService filmService() {
        return new FilmService(filmStorage, userStorage);
    }

    @Bean
    public UserService userService() {
        return new UserService(userStorage);
    }
}
