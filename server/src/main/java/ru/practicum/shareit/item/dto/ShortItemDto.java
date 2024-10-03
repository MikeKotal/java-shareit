package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ShortItemDto {
    Long getId();
    String getName();
    Long getOwnerId();
}
