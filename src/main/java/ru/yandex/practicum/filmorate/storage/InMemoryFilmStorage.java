package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new ConcurrentHashMap<>();
    private Long nextId = 1L;

    @Override
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов в хранилище: {}", films.size());
        return List.copyOf(films.values());
    }

    @Override
    public Film findById(Long id) {
        Film film = films.get(id);
        if (film == null) {
            log.warn("Фильм с id={} не найден", id);
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        return film;
    }

    @Override
    public Film create(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Фильм создан с id={}", film.getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        Film exFilm = findById(film.getId());
        if (film.getName() != null) exFilm.setName(film.getName());
        if (film.getDescription() != null) exFilm.setDescription(film.getDescription());
        if (film.getReleaseDate() != null) exFilm.setReleaseDate(film.getReleaseDate());
        if (film.getDuration() != null) exFilm.setDuration(film.getDuration());
        log.info("Фильм с id={} обновлён", film.getId());
        return exFilm;
    }

    @Override
    public void delete(Long id) {
        Film removed = films.remove(id);
        if (removed == null) {
            log.warn("Попытка удалить несуществующий фильм id={}", id);
            throw new NotFoundException("Фильм с id=" + id + " не найден");
        }
        log.info("Фильм с id={} удалён", id);
    }
}
