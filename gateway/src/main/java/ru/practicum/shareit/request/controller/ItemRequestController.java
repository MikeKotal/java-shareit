package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.RequestClient;
import ru.practicum.shareit.request.dto.RequestDto;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestClient requestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") @Positive Long requesterId,
                                                    @Valid @RequestBody RequestDto requestDto) {
        return requestClient.createItemRequest(requesterId, requestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequestsByRequesterId(@RequestHeader("X-Sharer-User-Id") @Positive Long requesterId) {
        return requestClient.getItemRequestsByRequesterId(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                  @RequestParam(defaultValue = "0") Integer page) {
        return requestClient.getItemRequests(userId, page);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                 @PathVariable @Positive Long requestId) {
        return requestClient.getItemRequest(userId, requestId);
    }
}
