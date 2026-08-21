package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.OnCreate;
import ru.yandex.practicum.filmorate.validator.OnUpdate;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Запрошен список фильмов");
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable @Positive Long id) {
        log.info("Запрошен фильм с id={}", id);
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@Validated(OnCreate.class) @RequestBody Film film) {
        log.info("Создание фильма: {}", film);
        Film created = filmService.createFilm(film);
        log.info("Фильм создан с id={}", created.getId());
        return created;
    }

    @PutMapping
    public Film updateFilm(@Validated(OnUpdate.class) @RequestBody Film film) {
        log.info("Обновление фильма с id={}", film.getId());
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        log.info("Запрос на лайк фильма id={} от пользователя id={}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable @Positive Long id, @PathVariable @Positive Long userId) {
        log.info("Запрос на удаление лайка фильма id={} от пользователя id={}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Запрошен список популярных фильмов, count={}", count);
        return filmService.getPopular(count);
    }
}
