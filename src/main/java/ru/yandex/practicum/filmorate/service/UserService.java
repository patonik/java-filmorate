package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int id, int friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);
        if (user == null || friend == null) {
            return null;
        }
        user.getFriends().add(friend);
        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);
        if (user == null) {
            return null;
        }
        user.getFriends().remove(friend);
        return user;
    }

    public List<User> getFriends(int id) {
        User user = userStorage.getById(id);
        if (user == null) {
            return null;
        }
        return user.getFriends().stream().toList();
    }

    public List<User> getFriendIntersection(int id, int otherId) {
        User user = userStorage.getById(id);
        User other = userStorage.getById(otherId);
        if (user == null || other == null) {
            return null;
        }
        Set<User> commonFriends = new HashSet<>(user.getFriends());
        commonFriends.retainAll(other.getFriends());
        return commonFriends.stream().toList();
    }
}
