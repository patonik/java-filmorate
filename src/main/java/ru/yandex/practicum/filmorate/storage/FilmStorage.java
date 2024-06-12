package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film delete(Film film);

    Film update(Film film);

    List<Film> getAll();

    Film getById(int id);

    List<Genre> getGenres();

    Genre getGenreById(int id);

    List<Mpa> getMpas();

    Mpa getMpaById(int id);
}
