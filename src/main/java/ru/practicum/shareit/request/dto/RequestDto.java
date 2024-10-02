package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RequestDto {

    @NotBlank(message = "Описание запроса на вещь не должно быть пустым")
    @Size(max = 1000, message = "Максимальное количество символов = 1000")
    private String description;
}
