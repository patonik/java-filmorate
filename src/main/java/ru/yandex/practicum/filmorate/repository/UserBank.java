package ru.yandex.practicum.filmorate.repository;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component("userBank")
public class UserBank {
    private static UserBank instance;
    private final Map<Integer, User> userBank = new ConcurrentHashMap<>();
    private final AtomicInteger nextInt = new AtomicInteger(1);

    public static synchronized UserBank getInstance() {
        if (instance == null) {
            instance = new UserBank();
        }
        return instance;
    }

    public User createUser(User user) {
        int i = nextInt.getAndIncrement();
        user.setId(i);
        return userBank.put(i, user);
    }

    public User updateUser(int id, User user) {
        if (!userBank.containsKey(id)) {
            return user;
        }
        return userBank.put(id, user);
    }

    public List<User> getUsers() {
        return userBank.values().stream().toList();
    }
}
