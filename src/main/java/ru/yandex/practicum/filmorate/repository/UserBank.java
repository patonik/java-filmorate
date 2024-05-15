package ru.yandex.practicum.filmorate.repository;


import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserBank {
    private static UserBank instance;
    private final Map<Integer, User> userBank = new ConcurrentHashMap<>();
    private AtomicInteger nextId = new AtomicInteger(0);

    public static synchronized UserBank getInstance() {
        if (instance == null) {
            instance = new UserBank();
        }
        return instance;
    }

    public User createUser(User user) {
        int id = nextId.getAndIncrement();
        user.setId(id);
        return userBank.put(id, user);
    }

    public User updateUser(int id, User user) {
        if (!userBank.containsKey(id)) {
            return user;
        }
        return userBank.put(id, user);
    }

    public User[] getUsers() {
        return userBank.values().toArray(new User[0]);
    }
}
