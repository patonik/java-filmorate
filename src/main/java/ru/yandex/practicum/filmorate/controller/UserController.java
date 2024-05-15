package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserBank;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    UserBank userBank = UserBank.getInstance();

    @GetMapping(path = "/")
    public User[] getUsers() {
        return userBank.getUsers();
    }

    @PostMapping(path = "new", consumes = "application/json", produces = "application/json")
    public User createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorList) {
            log.atWarn().log(fieldError.getDefaultMessage());
        }
        if (errorList.isEmpty()) {
            User result = userBank.createUser(user);
            if (result == null) {
                log.atInfo().log("user created");
            } else {
                log.atInfo().log("userBank's state is not valid");
            }
            return result;
        } else {
            return null;
        }
    }

    @PostMapping(path = "/{id}/edit", consumes = "application/json", produces = "application/json")
    public User editUser(@Valid @RequestBody User user, @PathVariable("id") int id, BindingResult bindingResult) {
        List<FieldError> errorList = bindingResult.getFieldErrors();
        for (FieldError fieldError : errorList) {
            log.atWarn().log(fieldError.getDefaultMessage());
        }
        if (errorList.isEmpty()) {
            User result = userBank.updateUser(id, user);
            if (result.equals(user)) {
                log.atInfo().log("user was not updated");
            } else {
                log.atInfo().log("user was updated");
            }
            return result;
        } else {
            return null;
        }
    }
}
