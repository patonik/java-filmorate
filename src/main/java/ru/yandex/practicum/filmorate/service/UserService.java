package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    User addFriend(int id, int friendId);

    User deleteFriend(int id, int friendId);

    List<User> getFriends(int id);

    List<User> getFriendIntersection(int id, int otherId, boolean confirmed);
}
