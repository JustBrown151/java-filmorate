package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.OnCreate;
import ru.yandex.practicum.filmorate.validator.OnUpdate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private Long nextId = 1L;

    @GetMapping
    public List<Film> getFilms() {
        log.info("Запрошен список фильмов, всего: {}", films.size());
        return films;
    }

    @PostMapping
    public Film createFilm(@Validated(OnCreate.class) @RequestBody Film film) {
        log.info("Создание фильма: {}", film);
        film.setId(nextId);
        nextId++;
        films.add(film);
        log.info("Фильм создан с id={}", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Validated(OnUpdate.class) @RequestBody Film film) {
        log.info("Обновление фильма с id={}", film.getId());
        Film exFilm = films.stream().filter(f -> f.getId().equals(film.getId())).findFirst().orElseThrow(() -> {
            log.warn("Фильм с id={} не найден", film.getId());
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм не найден");
        });
        if (film.getName() != null) exFilm.setName(film.getName());
        if (film.getDescription() != null) exFilm.setDescription(film.getDescription());
        if (film.getReleaseDate() != null) exFilm.setReleaseDate(film.getReleaseDate());
        if (film.getDuration() != null) exFilm.setDuration(film.getDuration());
        log.info("Фильм с id={} обновлён", film.getId());
        return film;
    }
}
