package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.OnCreate;
import ru.yandex.practicum.filmorate.validator.OnUpdate;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Запрошен список пользователей");
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable @Positive Long id) {
        log.info("Запрошен пользователь с id={}", id);
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@Validated(OnCreate.class) @RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        User created = userService.createUser(user);
        log.info("Пользователь создан с id={}", created.getId());
        return created;
    }

    @PutMapping
    public User updateUser(@Validated(OnUpdate.class) @RequestBody User user) {
        log.info("Обновление пользователя с id={}", user.getId());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive Long id, @PathVariable @Positive Long friendId) {
        log.info("Запрос на добавление в друзья: id={}, friendId={}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable @Positive Long id, @PathVariable @Positive Long friendId) {
        log.info("Запрос на удаление из друзей: id={}, friendId={}", id, friendId);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable @Positive Long id) {
        log.info("Запрошен список друзей пользователя id={}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable @Positive Long id, @PathVariable @Positive Long otherId) {
        log.info("Запрошен список общих друзей пользователей id={} и id={}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
