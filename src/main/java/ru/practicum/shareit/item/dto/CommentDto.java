package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class CommentDto {

    private Long id;

    @NotBlank(message = "Текст отзыва не должен быть пустым")
    private String text;

    private String authorName;

    private String created;
}
