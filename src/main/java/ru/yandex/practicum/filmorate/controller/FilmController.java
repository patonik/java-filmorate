package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidArgumentsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmBank;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Autowired
    private FilmBank filmBank;

    @GetMapping()
    public List<Film> getFilms() {
        return filmBank.getFilms();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public Film createFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorList) {
            log.atWarn().log(fieldError.getDefaultMessage());
        }
        if (errorList.isEmpty()) {
            Film result = filmBank.createFilm(film);
            if (result == null) {
                log.atInfo().log("film created");
                return film;
            } else {
                log.atInfo().log("filmBank's state is not valid");
                return result;
            }
        } else {
            throw new InvalidArgumentsException();
        }
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public Film editFilm(@Valid @RequestBody Film film, @PathVariable("id") int id, BindingResult bindingResult) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorList) {
            log.atWarn().log(fieldError.getDefaultMessage());
        }
        if (errorList.isEmpty()) {
            Film result = filmBank.updateFilm(id, film);
            if (result.equals(film)) {
                log.atInfo().log("film was not updated");
                throw new NotFoundException();
            } else {
                log.atInfo().log("film was updated");
            }
            return result;
        } else {
            return null;
        }
    }
}
