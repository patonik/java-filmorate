package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.InvalidArgumentsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserBank;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    @Autowired
    private UserBank userBank;

    @GetMapping()
    public List<User> getUsers() {
        return userBank.getUsers();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public User createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorList) {
            log.atWarn().log(fieldError.getDefaultMessage());
        }
        if (errorList.isEmpty()) {
            User result = userBank.createUser(user);
            if (result == null) {
                log.atInfo().log("user created");
                return user;
            } else {
                log.atInfo().log("userBank's state is not valid");
                return result;
            }
        } else {
            throw new InvalidArgumentsException();
        }
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    public User editUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorList) {
            log.atWarn().log(fieldError.getDefaultMessage());
        }
        if (errorList.isEmpty()) {
            User result = userBank.updateUser(user.getId(), user);
            if (result.equals(user)) {
                log.atInfo().log("user was not updated");
                throw new NotFoundException();
            } else {
                log.atInfo().log("user was updated");
            }
            return result;
        } else {
            return null;
        }
    }
}
