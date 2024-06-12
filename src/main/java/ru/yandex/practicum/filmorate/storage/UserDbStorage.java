package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component("userDbStorage")
public class UserDbStorage extends DbStorage<User> implements UserStorage {
    private static final String INSERT =
        "INSERT INTO PUBLIC.\"user\" (EMAIL, LOGIN, NAME, BIRTHDATE) VALUES(?, ?, ?, ?);";
    private static final String DELETE = "DELETE FROM PUBLIC.\"user\" WHERE ID = ?;";
    private static final String UPDATE =
        "UPDATE PUBLIC.\"user\" SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDATE = ? WHERE ID = ?;";
    private static final String SELECT_ALL = "SELECT * FROM PUBLIC.\"user\";";
    private static final String SELECT_ONE = "SELECT * FROM PUBLIC.\"user\" WHERE ID = ?;";
    private static final String SELECT_LIKERS =
        "SELECT * FROM PUBLIC.\"user\" WHERE ID IN (SELECT \"user\" FROM PUBLIC.USER_FAVE_FILM WHERE FILM = ?);";
    private static final String INSERT_FRIEND =
        "INSERT INTO PUBLIC.USER_FRIEND (\"user\", FRIEND, CONFIRMED) VALUES(?, ?, ?);";
    private static final String SELECT_FRIENDS =
        "SELECT * FROM PUBLIC.\"user\" WHERE ID IN (SELECT FRIEND FROM PUBLIC.USER_FRIEND WHERE \"user\" = ? AND CONFIRMED = ?);";
    private static final String DELETE_FRIEND =
        "DELETE FROM PUBLIC.USER_FRIEND WHERE \"user\" = ? AND FRIEND = ? AND CONFIRMED = ?;";
    private static final String SELECT_COMMON_FRIENDS =
        "SELECT  * FROM \"user\" u WHERE ID IN (SELECT FRIEND FROM (SELECT FRIEND, count(FRIEND) FROM PUBLIC.USER_FRIEND WHERE (\"user\" = ? OR \"user\" = ?) AND CONFIRMED = ? GROUP BY FRIEND HAVING COUNT(FRIEND) > 1));";

    @Autowired
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public int insertFriend(int id, int friendId, boolean confirmed) {
        return jdbc.update(INSERT_FRIEND, id, friendId, confirmed);
    }

    public int deleteFriend(int id, int friendId, boolean confirmed) {
        return jdbc.update(DELETE_FRIEND, id, friendId, confirmed);
    }

    public List<User> getFriends(int id, boolean confirmed) {
        return findMany(SELECT_FRIENDS, id, confirmed);
    }

    public List<User> getCommonFriends(int id, int friendId, boolean confirmed) {
        return findMany(SELECT_COMMON_FRIENDS, id, friendId, confirmed);
    }

    public List<User> getLikers(int id) {
        return findMany(SELECT_LIKERS, id);
    }

    @Override
    public User create(User user) {
        user.setId(insert(INSERT, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday()));
        return user;
    }

    @Override
    public User delete(User user) {
        if (delete(DELETE, user.getId())) {
            return user;
        }
        return null;
    }

    @Override
    public User update(User user) {
        update(UPDATE, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public List<User> getAll() {
        return findMany(SELECT_ALL);
    }

    @Override
    public User getById(int id) {
        Optional<User> optionalUser = findOne(SELECT_ONE, id);
        if (optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        user.getFriends().addAll(getFriends(user.getId(), true));
        return user;
    }
}
