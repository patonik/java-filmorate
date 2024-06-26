package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User delete(User user);

    User update(User user);

    List<User> getAll();

    User getById(int id);
}
