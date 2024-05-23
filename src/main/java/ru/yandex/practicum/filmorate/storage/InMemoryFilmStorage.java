package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> filmBank = new ConcurrentHashMap<>();
    private final AtomicInteger nextInt = new AtomicInteger(1);

    @Override
    public Film delete(Film film) {
        return filmBank.remove(film.getId());
    }

    public Film create(Film film) {
        int i = nextInt.getAndIncrement();
        film.setId(i);
        return filmBank.put(i, film);
    }

    public Film update(Film film) {
        int id = film.getId();
        if (!filmBank.containsKey(id)) {
            return null;
        }
        return filmBank.put(id, film);
    }

    public List<Film> getAll() {
        return filmBank.values().stream().toList();
    }

    public Film getById(int id) {
        return filmBank.get(id);
    }
}
