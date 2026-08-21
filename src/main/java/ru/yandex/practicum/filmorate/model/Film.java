package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.OnCreate;
import ru.yandex.practicum.filmorate.validator.OnUpdate;
import ru.yandex.practicum.filmorate.validator.ReleaseDateAfter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;

    @NotBlank(message = "Название не может быть пустым", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @Size(max = 200, message = "Максимальная длина описания — 200 символов", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(groups = OnCreate.class)
    @ReleaseDateAfter(groups = {OnCreate.class, OnUpdate.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotNull(groups = OnCreate.class)
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer duration;

    private final Set<Long> likes = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
