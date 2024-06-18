package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.List;

@Service("filmDbService")
public class FilmDbService implements FilmService {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmDbService(FilmDbStorage filmStorage,
                         UserDbStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(int id, int userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);
        if (user == null || film == null) {
            return null;
        }
        filmStorage.insertLike(id, userId);
        film.getUsersLiked().addAll(userStorage.getLikers(id));
        return film;
    }

    public Film deleteLike(int id, int userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);
        if (film == null || user == null) {
            return null;
        }
        filmStorage.deleteLike(id, userId);
        film.getUsersLiked().addAll(userStorage.getLikers(id));
        return film;
    }

    public List<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }
}
