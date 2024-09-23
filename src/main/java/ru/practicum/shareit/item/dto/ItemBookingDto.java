package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ItemBookingDto {

    private Long id;

    private String name;

    private String description;

    private Boolean available;

    @JsonInclude
    private String lastBooking;

    @JsonInclude
    private String nextBooking;

    private List<CommentDto> comments;
}
