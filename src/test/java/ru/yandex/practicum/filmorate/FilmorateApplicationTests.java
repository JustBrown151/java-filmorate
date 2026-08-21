package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.OnCreate;
import ru.yandex.practicum.filmorate.validator.OnUpdate;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {

    @Test
    void contextLoads() {
    }

    @Nested
    @DisplayName("Валидация модели Film")
    class FilmValidationTest {

        private static ValidatorFactory factory;
        private static Validator validator;

        @BeforeAll
        static void setUpValidator() {
            factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

        @AfterAll
        static void closeFactory() {
            factory.close();
        }

        private Film validFilm() {
            return new Film(null, "Film", "Test Film", LocalDate.of(2014, 11, 7), 169);
        }

        @Test
        @DisplayName("Корректный фильм при создании не вызывает нарушений")
        void validFilm_onCreate_hasNoViolations() {
            Set<ConstraintViolation<Film>> violations = validator.validate(validFilm(), OnCreate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("id указан при создании — ошибка")
        void idPresent_onCreate_isRejected() {
            Film film = validFilm();
            film.setId(1L);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
        }

        @Test
        @DisplayName("name = null при создании — ошибка")
        void nameNull_onCreate_isRejected() {
            Film film = validFilm();
            film.setName(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        }

        @Test
        @DisplayName("name состоит из пробелов — ошибка")
        void nameBlank_onCreate_isRejected() {
            Film film = validFilm();
            film.setName("   ");
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        }

        @Test
        @DisplayName("description = null при создании — допустимо")
        void descriptionNull_onCreate_isAccepted() {
            Film film = validFilm();
            film.setDescription(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("description ровно 200 символов — граница, допустимо")
        void description200Chars_onCreate_isAccepted() {
            Film film = validFilm();
            film.setDescription("a".repeat(200));
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("description 201 символ — превышение границы, ошибка")
        void description201Chars_onCreate_isRejected() {
            Film film = validFilm();
            film.setDescription("a".repeat(201));
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        }

        @Test
        @DisplayName("releaseDate = null при создании — ошибка")
        void releaseDateNull_onCreate_isRejected() {
            Film film = validFilm();
            film.setReleaseDate(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("releaseDate")));
        }

        @Test
        @DisplayName("releaseDate = 28.12.1895 — граница, допустимо")
        void releaseDateOnBoundary_isAccepted() {
            Film film = validFilm();
            film.setReleaseDate(LocalDate.of(1895, 12, 28));
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("releaseDate = 27.12.1895 — на день раньше границы, ошибка")
        void releaseDateOneDayBeforeBoundary_isRejected() {
            Film film = validFilm();
            film.setReleaseDate(LocalDate.of(1895, 12, 27));
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("releaseDate")));
        }

        @Test
        @DisplayName("duration = null при создании — ошибка")
        void durationNull_onCreate_isRejected() {
            Film film = validFilm();
            film.setDuration(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
        }

        @Test
        @DisplayName("duration = 0 — граница, ошибка")
        void durationZero_isRejected() {
            Film film = validFilm();
            film.setDuration(0);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
        }

        @Test
        @DisplayName("duration отрицательная — ошибка")
        void durationNegative_isRejected() {
            Film film = validFilm();
            film.setDuration(-1);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("duration")));
        }

        @Test
        @DisplayName("duration = 1 — граница, допустимо")
        void durationOne_isAccepted() {
            Film film = validFilm();
            film.setDuration(1);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("id = null при обновлении — ошибка")
        void idMissing_onUpdate_isRejected() {
            Film film = validFilm();
            film.setId(null);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnUpdate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
        }

        @Test
        @DisplayName("При обновлении можно передать только id и name — остальные поля не обязательны")
        void onUpdate_onlyIdAndName_otherFieldsOmitted_isAccepted() {
            Film film = new Film();
            film.setId(1L);
            film.setName("New name");
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnUpdate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("name = null при обновлении всё равно является ошибкой (в отличие от других полей)")
        void onUpdate_nameNull_isStillRejected() {
            Film film = new Film();
            film.setId(1L);
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnUpdate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("name")));
        }

        @Test
        @DisplayName("description слишком длинное при обновлении — ошибка")
        void onUpdate_descriptionTooLong_isRejected() {
            Film film = new Film();
            film.setId(1L);
            film.setName("Name");
            film.setDescription("a".repeat(201));
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnUpdate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("description")));
        }

        @Test
        @DisplayName("Пустой запрос на создание — ошибки по всем обязательным полям")
        void allFieldsBlank_onCreate_producesViolationForEveryField() {
            Film film = new Film();
            Set<ConstraintViolation<Film>> violations = validator.validate(film, OnCreate.class);
            Set<String> violatedFields = violations.stream().map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());
            assertEquals(Set.of("name", "releaseDate", "duration"), violatedFields);
        }
    }

    @Nested
    @DisplayName("Валидация модели User")
    class UserValidationTest {

        private static ValidatorFactory factory;
        private static Validator validator;

        @BeforeAll
        static void setUpValidator() {
            factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }

        @AfterAll
        static void closeFactory() {
            factory.close();
        }

        private User validUser() {
            return new User(null, "user@example.com", "userlogin", "User Name", LocalDate.of(2000, 1, 1));
        }

        @Test
        @DisplayName("Корректный пользователь при создании не вызывает нарушений")
        void validUser_onCreate_hasNoViolations() {
            Set<ConstraintViolation<User>> violations = validator.validate(validUser(), OnCreate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("id указан при создании — ошибка")
        void idPresent_onCreate_isRejected() {
            User user = validUser();
            user.setId(1L);
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
        }

        @Test
        @DisplayName("email = null при создании — ошибка")
        void emailNull_onCreate_isRejected() {
            User user = validUser();
            user.setEmail(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        }

        @Test
        @DisplayName("email без символа @ — ошибка")
        void emailWithoutAtSign_isRejected() {
            User user = validUser();
            user.setEmail("user.example.com");
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        }

        @Test
        @DisplayName("email = \"\" при создании — текущая реализация это пропускает (@Email не проверяет пустую строку)")
        void emailBlank_onCreate_isCurrentlyAccepted() {
            User user = validUser();
            user.setEmail("");
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("email")));
        }

        @Test
        @DisplayName("login = null при создании — ошибка")
        void loginNull_onCreate_isRejected() {
            User user = validUser();
            user.setLogin(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
        }

        @Test
        @DisplayName("login состоит из пробелов — ошибка")
        void loginBlank_onCreate_isRejected() {
            User user = validUser();
            user.setLogin("   ");
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
        }

        @Test
        @DisplayName("login содержит пробел внутри — ошибка")
        void loginWithSpaces_isRejected() {
            User user = validUser();
            user.setLogin("user login");
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
        }

        @Test
        @DisplayName("login без пробелов — допустимо")
        void loginWithoutSpaces_isAccepted() {
            User user = validUser();
            user.setLogin("user_login-42");
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("name = null при создании — не ошибка (контроллер подставит login вместо пустого имени)")
        void nameNull_onCreate_isAccepted() {
            User user = validUser();
            user.setName(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("name")));
        }

        @Test
        @DisplayName("name = \"\" при создании — не ошибка (контроллер подставит login вместо пустого имени)")
        void nameBlank_onCreate_isAccepted() {
            User user = validUser();
            user.setName("");
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("name")));
        }

        @Test
        @DisplayName("birthday = null при создании — ошибка")
        void birthdayNull_onCreate_isRejected() {
            User user = validUser();
            user.setBirthday(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthday")));
        }

        @Test
        @DisplayName("birthday = сегодня — ошибка (день рождения не может быть в будущем или сегодня)")
        void birthdayToday_isRejected() {
            User user = validUser();
            user.setBirthday(LocalDate.now());
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthday")));
        }

        @Test
        @DisplayName("birthday = вчера — граница, допустимо")
        void birthdayYesterday_isAccepted() {
            User user = validUser();
            user.setBirthday(LocalDate.now().minusDays(1));
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("birthday в будущем — ошибка")
        void birthdayInFuture_isRejected() {
            User user = validUser();
            user.setBirthday(LocalDate.now().plusDays(1));
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnCreate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("birthday")));
        }

        @Test
        @DisplayName("id = null при обновлении — ошибка")
        void idMissing_onUpdate_isRejected() {
            User user = validUser();
            user.setId(null);
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("id")));
        }

        @Test
        @DisplayName("При обновлении можно передать только id и login — остальные поля не обязательны")
        void onUpdate_onlyIdAndLogin_otherFieldsOmitted_isAccepted() {
            User user = new User();
            user.setId(1L);
            user.setLogin("newlogin");
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);
            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("login = null при обновлении всё равно является ошибкой (в отличие от других полей)")
        void onUpdate_loginNull_isStillRejected() {
            User user = new User();
            user.setId(1L);
            Set<ConstraintViolation<User>> violations = validator.validate(user, OnUpdate.class);
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("login")));
        }
    }
}
