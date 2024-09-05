package ru.practicum.shareit.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserValidationTests {

	@Autowired
	private Validator validator;
	private UserDto user;

	@BeforeEach
	public void setUp() {
		user = UserDto.builder()
				.id(1L)
				.name("Name")
				.email("test@test.ru")
				.build();
	}

	@Test
	public void checkSuccessUserValidation() {
		Set<ConstraintViolation<UserDto>> result = validator.validate(user);
		assertTrue(result.isEmpty(), "Ошибки по валидации быть не должно");
	}

	@Test
	public void whenNameIsNullThenValidationIsFailed() {
		user.setName("");
		checkAssert("name", "Имя не должно быть пустым");
	}

	@Test
	public void whenEmailIsNullThenValidationIsFailed() {
		user.setEmail("");
		checkAssert("email", "Email не должен быть пустым");
	}

	@Test
	public void whenEmailIsInvalidThenValidationIsFailed() {
		user.setEmail("test@");
		checkAssert("email", "Email имеет некорректный формат");
	}

	private void checkAssert(String field, String errorMessage) {
		List<ConstraintViolation<UserDto>> result = List.copyOf(validator.validate(user));
		assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

		ConstraintViolation<UserDto> validationResult = result.getFirst();

		assertEquals(field, validationResult.getPropertyPath().toString());
		assertEquals(errorMessage, validationResult.getMessage());
	}
}
