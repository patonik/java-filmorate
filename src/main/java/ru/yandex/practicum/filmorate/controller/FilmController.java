package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getFilms() {
        return inMemoryFilmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        return inMemoryFilmStorage.getById(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Film createFilm(@Valid @RequestBody Film film) {
        Film result = inMemoryFilmStorage.create(film);
        if (result == null) {
            log.atInfo().log("film created");
        } else {
            log.atWarn().log("inMemoryFilmStorage's state is not valid");
        }
        return film;
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public Film editFilm(@Valid @RequestBody Film film) {
        Film result = inMemoryFilmStorage.update(film);
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

    @GetMapping("/films/popular?count={count}")
    public List<Film> getFirstCountFilms(@RequestParam(defaultValue = "10") int count) {
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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(NotFoundException e) {
        return Map.of("error", "not found",
            "error message",
            e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleNotFound(RuntimeException e) {
        return Map.of("error", "oops!",
            "error message",
            e.getMessage()
        );
    }

}
