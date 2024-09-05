package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ItemDto {
    private Long id;
    @NotBlank(message = "Наименование вещи не должно быть пустым")
    private String name;
    @NotBlank(message = "Описание вещи не должно быть пустым")
    private String description;
    @NotNull(message = "Параметр доступности не должен быть пустым")
    private Boolean available;
}
