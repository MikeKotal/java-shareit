package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl,
                         @Value("${shareit-server.requests}") String requestsUrl,
                         RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + requestsUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItemRequest(Long requesterId, RequestDto requestDto) {
        return post("", requesterId, requestDto);
    }

    public ResponseEntity<Object> getItemRequestsByRequesterId(Long requesterId) {
        return get("", requesterId);
    }

    public ResponseEntity<Object> getItemRequests(Long userId, Integer page) {
        Map<String, Object> parameters = Map.of(
                "page", page
        );
        return get("/all?page={page}", userId, parameters);
    }

    public ResponseEntity<Object> getItemRequest(Long userId, Long requestId) {
        return get(String.format("/%s", requestId), userId);
    }
}
