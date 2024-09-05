package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id", "owner"})
@Builder
public class Item {
    private Long id;
    @NotBlank(message = "Наименование вещи не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание вещи не должно быть пустым")
    private String description;
    @NotNull(message = "Параметр доступности не должен быть пустым")
    private Boolean available;
    private Long owner;
}
