package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> userBank = new ConcurrentHashMap<>();
    private final AtomicInteger nextInt = new AtomicInteger(1);

    @Override
    public User delete(User user) {
        return userBank.remove(user.getId());
    }

    @Override
    public User create(User user) {
        int i = nextInt.getAndIncrement();
        user.setId(i);
        return userBank.put(i, user);
    }

    @Override
    public User update(User user) {
        int id = user.getId();
        if (!userBank.containsKey(id)) {
            return null;
        }
        return userBank.put(id, user);
    }

    @Override
    public List<User> getAll() {
        return userBank.values().stream().toList();
    }

    @Override
    public User getById(int id) {
        return userBank.get(id);
    }
}
