package ru.yandex.practicum.filmorate.repository;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component("filmBank")
public class FilmBank {
    private static FilmBank instance;
    private final Map<Integer, Film> filmBank = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public static synchronized FilmBank getInstance() {
        if (instance == null) {
            instance = new FilmBank();
        }
        return instance;
    }

    public Film createFilm(Film film) {
        return filmBank.put(nextId.getAndIncrement(), film);
    }

    public Film updateFilm(int id, Film film) {
        return filmBank.put(id, film);
    }

    public List<Film> getFilms() {
        return filmBank.values().stream().toList();
    }
}
