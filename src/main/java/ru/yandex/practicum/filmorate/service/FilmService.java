package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage inMemoryUserStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage inMemoryUserStorage) {
        this.filmStorage = filmStorage;
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public Film addLike(int id, int userId) {
        Film film = filmStorage.getById(id);
        User user = inMemoryUserStorage.getById(userId);
        if (user == null || film == null) {
            return null;
        }
        film.getUsersLiked().add(user);
        return film;
    }

    public Film deleteLike(int id, int userId) {
        Film film = filmStorage.getById(id);
        User user = inMemoryUserStorage.getById(userId);
        if (film == null || user == null) {
            return null;
        }
        film.getUsersLiked().remove(user);
        return film;
    }

    public List<Film> getPopular(int count) {
        return filmStorage
            .getAll()
            .stream()
            .sorted(
                (x, y) -> y.getUsersLiked().size() - x.getUsersLiked().size()
            ).limit(count)
            .toList();
    }
}
