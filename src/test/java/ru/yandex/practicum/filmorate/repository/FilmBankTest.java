package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmBankTest {
    @Test
    @Order(1)
    void createFilm() {
        FilmBank filmBank = FilmBank.getInstance();
        assertThrows(Exception.class, () -> filmBank.createFilm(null));
        Film film = new Film();
        film.setName("Test Name");
        film.setId(0);
        film.setReleaseDate(LocalDateTime.now().minusYears(1).toLocalDate());
        film.setDescription("desc");
        film.setDuration(333);
        filmBank.createFilm(film);
        assertEquals(film, filmBank.getFilms().getFirst());
    }

    @Test
    @Order(2)
    void updateFilm() {
        FilmBank filmBank = FilmBank.getInstance();
        assertThrows(Exception.class, () -> filmBank.createFilm(null));
        Film film = new Film();
        film.setName("Test Name");
        film.setId(0);
        film.setReleaseDate(LocalDateTime.now().minusYears(1).toLocalDate());
        film.setDuration(333);
        film.setDescription("new desc");
        int length = filmBank.getFilms().size();
        filmBank.updateFilm(1, film);
        assertEquals(film, filmBank.getFilms().getFirst());
        assertEquals(length, filmBank.getFilms().size());
    }
}