package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemRequestDto {

    @NotBlank(message = "Наименование вещи не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание вещи не должно быть пустым")
    private String description;

    @NotNull(message = "Параметр доступности не должен быть пустым")
    private Boolean available;
}
