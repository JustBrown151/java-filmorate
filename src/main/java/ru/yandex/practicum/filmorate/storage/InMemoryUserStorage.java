package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private Long nextId = 1L;

    @Override
    public List<User> findAll() {
        log.debug("Текущее количество пользователей в хранилище: {}", users.size());
        return List.copyOf(users.values());
    }

    @Override
    public User findById(Long id) {
        User user = users.get(id);
        if (user == null) {
            log.warn("Пользователь с id={} не найден", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        return user;
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Пользователь создан с id={}", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        User exUser = findById(user.getId());
        if (user.getEmail() != null) exUser.setEmail(user.getEmail());
        if (user.getLogin() != null) exUser.setLogin(user.getLogin());
        if (user.getName() != null) {
            exUser.setName(user.getName().isBlank() ? exUser.getLogin() : user.getName());
        }
        if (user.getBirthday() != null) exUser.setBirthday(user.getBirthday());
        log.info("Пользователь с id={} обновлён", user.getId());
        return exUser;
    }

    @Override
    public void delete(Long id) {
        User removed = users.remove(id);
        if (removed == null) {
            log.warn("Попытка удалить несуществующего пользователя id={}", id);
            throw new NotFoundException("Пользователь с id=" + id + " не найден");
        }
        log.info("Пользователь с id={} удалён", id);
    }
}
