package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.BirthdateBefore;
import ru.yandex.practicum.filmorate.validator.OnCreate;
import ru.yandex.practicum.filmorate.validator.OnUpdate;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull(groups = OnUpdate.class)
    @Null(groups = OnCreate.class)
    private Long id;

    @NotNull(groups = OnCreate.class)
    @Email(groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotNull(groups = OnCreate.class)
    @NotBlank(groups = {OnCreate.class, OnUpdate.class})
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы", groups = {OnCreate.class, OnUpdate.class})
    private String login;

    @NotNull(groups = OnCreate.class)
    private String name;

    @NotNull(groups = OnCreate.class)
    @BirthdateBefore(groups = {OnCreate.class, OnUpdate.class})
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthday;
}
