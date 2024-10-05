package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.Status;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exceptions.ValidationException;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
                         @Value("${shareit-server.bookings}") String bookingsUrl,
                         RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + bookingsUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createBooking(Long bookerId, BookingRequestDto bookingDto) {
        return post("", bookerId, bookingDto);
    }

    public ResponseEntity<Object> approveBooking(Long bookerId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch(String.format("/%s?approved={approved}", bookingId), bookerId, parameters);
    }

    public ResponseEntity<Object> getBooking(Long bookerId, Long bookingId) {
        return get(String.format("/%s", bookingId), bookerId);
    }

    public ResponseEntity<Object> getBookingsByBookerId(Long bookerId, String state) {
        Map<String, Object> parameters;
        try {
            parameters = Map.of(
                    "state", Status.valueOf(state)
            );
        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format("Был передан невалидный тип фильтрации: %s", state));
        }
        return get("?state={state}", bookerId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwnerId(Long ownerId, String state) {
        Map<String, Object> parameters;
        try {
            parameters = Map.of(
                    "state", Status.valueOf(state)
            );
        } catch (IllegalArgumentException e) {
            throw new ValidationException(String.format("Был передан невалидный тип фильтрации: %s", state));
        }
        return get("/owner?state={state}", ownerId, parameters);
    }
}
