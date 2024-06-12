package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addLike(int id, int userId);

    Film deleteLike(int id, int userId);

    List<Film> getPopular(int count);
}
