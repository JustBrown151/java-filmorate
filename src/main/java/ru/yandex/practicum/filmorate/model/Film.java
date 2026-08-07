package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import ru.yandex.practicum.filmorate.validator.OnCreate;
import ru.yandex.practicum.filmorate.validator.OnUpdate;
import ru.yandex.practicum.filmorate.validator.ReleaseDateAfter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;

    @NotNull(groups = OnCreate.class)
    @NotBlank(message = "Название не может быть пустым", groups = {OnCreate.class, OnUpdate.class})
    private String name;

    @NotNull(groups = OnCreate.class)
    @Size(max = 200, message = "Максимальная длина описания — 200 символов", groups = {OnCreate.class, OnUpdate.class})
    private String description;

    @NotNull(groups = OnCreate.class)
    @ReleaseDateAfter(groups = {OnCreate.class, OnUpdate.class})
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate releaseDate;

    @NotNull(groups = OnCreate.class)
    @Positive(groups = {OnCreate.class, OnUpdate.class})
    private Integer duration;
}
