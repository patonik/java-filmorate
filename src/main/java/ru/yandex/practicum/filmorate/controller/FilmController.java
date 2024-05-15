package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmBank;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmBank filmBank = FilmBank.getInstance();

    @GetMapping()
    public Film[] getFilms() {
        return filmBank.getFilms();
    }

    @PostMapping(path = "new", consumes = "application/json", produces = "application/json")
    public Film createFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorList) {
            log.atWarn().log(fieldError.getDefaultMessage());
        }
        if (errorList.isEmpty()) {
            Film result = filmBank.createFilm(film);
            if (result == null) {
                log.atInfo().log("film created");
            } else {
                log.atInfo().log("filmBank's state is not valid");
            }
            return result;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/{id}/edit", consumes = "application/json", produces = "application/json")
    public Film editFilm(@Valid @RequestBody Film film, @PathVariable("id") int id, BindingResult bindingResult) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorList) {
            log.atWarn().log(fieldError.getDefaultMessage());
        }
        if (errorList.isEmpty()) {
            Film result = filmBank.updateFilm(id, film);
            if (result.equals(film)) {
                log.atInfo().log("film was not updated");
            } else {
                log.atInfo().log("film was updated");
            }
            return result;
        } else {
            return null;
        }
    }
}
