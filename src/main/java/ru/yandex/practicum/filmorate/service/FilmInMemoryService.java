package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmInMemoryService implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmInMemoryService(@Qualifier("filmInMemoryStorage") FilmStorage filmStorage,
                               @Qualifier("userInMemoryStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLike(int id, int userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);
        if (user == null || film == null) {
            return null;
        }
        film.getUsersLiked().add(user);
        return film;
    }

    public Film deleteLike(int id, int userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);
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
