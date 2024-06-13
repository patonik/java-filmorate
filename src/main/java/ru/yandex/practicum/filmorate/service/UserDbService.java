package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.List;

@Slf4j
@Service
public class UserDbService implements UserService {
    private final UserDbStorage userStorage;

    @Autowired
    public UserDbService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addFriend(int id, int friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);
        if (user == null || friend == null) {
            return null;
        }
        userStorage.insertFriend(id, friendId, true);
        userStorage.insertFriend(friendId, id, false);
        user.getFriends().addAll(userStorage.getFriends(id, true));
        return user;
    }

    public User deleteFriend(int id, int friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);
        if (user == null || friend == null) {
            return null;
        }
        userStorage.deleteFriend(id, friendId, true);
        user.getFriends().addAll(userStorage.getFriends(id, true));
        return user;
    }

    public List<User> getFriends(int id) {
        User user = userStorage.getById(id);
        if (user == null) {
            return null;
        }
        user.getFriends().addAll(userStorage.getFriends(id, true));
        return user.getFriends().stream().toList();
    }

    public List<User> getFriendIntersection(int id, int otherId, boolean confirmed) {
        User user = userStorage.getById(id);
        User other = userStorage.getById(otherId);
        if (user == null || other == null) {
            return null;
        }
        return userStorage.getCommonFriends(id, otherId, true);
    }
}
