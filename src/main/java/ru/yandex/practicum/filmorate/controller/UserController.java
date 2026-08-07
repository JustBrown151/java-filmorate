package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.OnCreate;
import ru.yandex.practicum.filmorate.validator.OnUpdate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final List<User> users = new ArrayList<>();
    private Long nextId = 0L;

    @GetMapping
    public List<User> getUsers() {
        log.info("Запрошен список пользователей, всего: {}", users.size());
        return users;
    }

    @PostMapping
    public User createUser(@Validated(OnCreate.class) @RequestBody User user) {
        log.info("Создание пользователя: {}", user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextId);
        nextId++;
        users.add(user);
        log.info("Пользователь создан с id={}", user.getId());
        return user;
    }

    @PatchMapping
    public User updateUser(@Validated(OnUpdate.class) @RequestBody User user) {
        log.info("Обновление пользователя с id={}", user.getId());
        User exUser = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElseThrow(() -> {
            log.warn("Пользователь с id={} не найден", user.getId());
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден");
        });
        if (user.getName() != null && user.getName().isBlank()) {
            user.setName(user.getLogin() != null ? user.getLogin() : exUser.getLogin());
        }
        if (user.getEmail() != null) exUser.setEmail(user.getEmail());
        if (user.getLogin() != null) exUser.setLogin(user.getLogin());
        if (user.getName() != null) exUser.setName(user.getName());
        if (user.getBirthday() != null) exUser.setBirthday(user.getBirthday());
        log.info("Пользователь с id={} обновлён", user.getId());
        return user;
    }
}
