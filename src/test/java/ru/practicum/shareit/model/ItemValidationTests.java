package ru.practicum.shareit.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ItemValidationTests {

    @Autowired
    private Validator validator;
    private ItemDto item;

    @BeforeEach
    public void setUp() {
        item = ItemDto.builder()
                .id(1L)
                .name("Name")
                .description("Description")
                .available(Boolean.TRUE)
                .build();
    }

    @Test
    public void checkSuccessItemValidation() {
        Set<ConstraintViolation<ItemDto>> result = validator.validate(item);
        assertTrue(result.isEmpty(), "Ошибки по валидации быть не должно");
    }

    @Test
    public void whenNameIsNullThenValidationIsFailed() {
        item.setName("");
        checkAssert("name", "Наименование вещи не должно быть пустым");
    }

    @Test
    public void whenDescriptionIsNullThenValidationIsFailed() {
        item.setDescription("");
        checkAssert("description", "Описание вещи не должно быть пустым");
    }

    @Test
    public void whenAvailableIsNullThenValidationIsFailed() {
        item.setAvailable(null);
        checkAssert("available", "Параметр доступности не должен быть пустым");
    }

    private void checkAssert(String field, String errorMessage) {
        List<ConstraintViolation<ItemDto>> result = List.copyOf(validator.validate(item));
        assertEquals(1, result.size(), "Отсутствует ошибка по валидации поля");

        ConstraintViolation<ItemDto> validationResult = result.getFirst();

        assertEquals(field, validationResult.getPropertyPath().toString());
        assertEquals(errorMessage, validationResult.getMessage());
    }
}
