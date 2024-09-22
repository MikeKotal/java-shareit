package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class BookingDto {

    private Long id;

    @NotBlank(message = "Идентификатор бронируемой вещи не должен быть пустым")
    private Long itemId;

    @NotBlank(message = "Дата начала бронирования не должна быть пустой")
    @FutureOrPresent(message = "Дата начала бронирования должны быть текущей или будущей")
    private String start;

    @Future(message = "Дата окончания бронирования должна быть в будущем")
    private String end;

    private Status status;

    private ItemDto item;

    private UserDto booker;
}
