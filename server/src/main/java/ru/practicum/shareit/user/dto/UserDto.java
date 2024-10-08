package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
