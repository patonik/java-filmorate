package ru.yandex.practicum.filmorate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

@Configuration
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate"})
public class AppConfig {

    @Bean
    @Scope("singleton")
    public InMemoryUserStorage inMemoryUserStorage() {
        return new InMemoryUserStorage();
    }

    @Bean
    @Scope("singleton")
    public InMemoryFilmStorage inMemoryFilmStorage() {
        return new InMemoryFilmStorage();
    }
}
