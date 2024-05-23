package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping()
    public List<User> getUsers() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUsers(@PathVariable int id) {
        return userStorage.getById(id);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public User createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        User result = userStorage.create(user);
        if (result == null) {
            log.atInfo().log("user created");
        } else {
            log.atWarn().log("UserStorage's state is not valid");
        }
        return user;
    }

    @PutMapping(value = "", consumes = "application/json", produces = "application/json")
    public User editUser(@Valid @RequestBody User user) {
        User result = userStorage.update(user);
        if (result == null) {
            log.atInfo().log("user was not updated");
            throw new NotFoundException("such user is not found");
        } else {
            log.atInfo().log("user was updated");
            return user;
        }
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") int id, @PathVariable("friendId") int friendId) {
        log.atInfo().log("started adding friend");
        if (id == friendId) {
            throw new ValidationException("everyone is their own nemesis");
        }
        User user = userService.addFriend(id, friendId);
        if (user == null) {
            throw new NotFoundException("either/both values invalid");
        }
        return user;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userService.addFriend(id, friendId);
        if (user == null) {
            throw new NotFoundException("either/both values invalid");
        }
        return user;
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        List<User> friends = userService.getFriends(id);
        if (friends == null) {
            log.atWarn().log("id not found");
            throw new NotFoundException("user invalid");
        }
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (id == otherId) {
            return getFriends(id);
        }
        List<User> friendIntersection = userService.getFriendIntersection(id, otherId);
        if (friendIntersection == null) {
            throw new NotFoundException("either/both values invalid");
        }
        return friendIntersection;
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
            "unknown error"
        );
    }
}
