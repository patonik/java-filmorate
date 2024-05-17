package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserBankTest {

    @Test
    @Order(1)
    void createUser() {
        UserBank userBank = UserBank.getInstance();
        assertThrows(Exception.class, () -> userBank.createUser(null));
        User user = new User();
        user.setName("Test Name");
        user.setId(0);
        user.setBirthday(LocalDate.MIN);
        user.setLogin("Login");
        user.setEmail("email@test.com");
        userBank.createUser(user);
        assertEquals(user, userBank.getUsers().getFirst());
    }

    @Test
    @Order(2)
    void updateUser() {
        UserBank userBank = UserBank.getInstance();
        assertThrows(Exception.class, () -> userBank.createUser(null));
        User user = new User();
        user.setName("Test Name");
        user.setId(0);
        user.setBirthday(LocalDate.MIN);
        user.setLogin("Login");
        user.setEmail("new@email.com");
        int length = userBank.getUsers().size();
        userBank.updateUser(1, user);
        assertEquals(user, userBank.getUsers().getFirst());
        assertEquals(length, userBank.getUsers().size());
    }
}