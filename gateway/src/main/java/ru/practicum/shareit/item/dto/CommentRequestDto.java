package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestDto {

    @NotBlank(message = "Текст отзыва не должен быть пустым")
    @Size(max = 1000, message = "Максимальное количество символов = 1000")
    private String text;
}
