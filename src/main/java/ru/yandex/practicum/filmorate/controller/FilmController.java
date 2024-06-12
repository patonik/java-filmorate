package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidArgumentsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                          @Qualifier("filmDbService") FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmStorage.getAll();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable int id) {
        return filmStorage.getById(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        return filmStorage.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpas() {
        return filmStorage.getMpas();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable int id) {
        return filmStorage.getMpaById(id);
    }

    @PostMapping(value = "/films", consumes = "application/json", produces = "application/json")
    public Film createFilm(@Valid @RequestBody Film film) {
        log.atInfo().log(film.toString());
        Film result = filmStorage.create(film);
        return film;
    }

    @PutMapping(value = "/films", consumes = "application/json", produces = "application/json")
    public Film editFilm(@Valid @RequestBody Film film) {
        Film result = filmStorage.update(film);
        if (result == null) {
            log.atInfo().log("film was not updated");
            throw new NotFoundException("film not found");
        }
        log.atInfo().log("film was updated");
        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film likeFilm(@PathVariable int id, @PathVariable int userId) {
        Film film = filmService.addLike(id, userId);
        if (film == null) {
            throw new NotFoundException("either/both values invalid");
        }
        return film;
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film unlikeFilm(@PathVariable int id, @PathVariable int userId) {
        Film film = filmService.deleteLike(id, userId);
        if (film == null) {
            throw new NotFoundException("either/both values invalid");
        }
        return film;
    }

    @GetMapping("/films/popular")
    public List<Film> getFirstCountFilms(@RequestParam(name = "count", defaultValue = "10") int count) {
        return filmService.getPopular(count);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(ValidationException e) {
        return Map.of("error", "object not valid",
            "error message",
            e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleInvalidArgument(InvalidArgumentsException e) {
        return Map.of("error", "object not valid",
            "error message",
            e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(NotFoundException e) {
        return Map.of("error", "not found",
            "error message",
            e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleRuntime(RuntimeException e) {
        return Map.of("error", "oops!",
            "error message",
            e.getMessage()
        );
    }

}
