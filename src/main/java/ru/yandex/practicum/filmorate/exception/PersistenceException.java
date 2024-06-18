package ru.yandex.practicum.filmorate.exception;

public class PersistenceException extends RuntimeException {
    public PersistenceException() {
        super();
    }

    public PersistenceException(String message) {
        super(message);
    }
}
