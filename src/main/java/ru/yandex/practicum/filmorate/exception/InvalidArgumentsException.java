package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidArgumentsException extends RuntimeException {
    public InvalidArgumentsException() {
    }

    public InvalidArgumentsException(String message) {
        super(message);
    }
}
