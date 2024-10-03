package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl,
                      @Value("${shareit-server.items}") String itemsUrl,
                      RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + itemsUrl))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> createItem(Long ownerId, ItemRequestDto itemRequestDto) {
        return post("", ownerId, itemRequestDto);
    }

    public ResponseEntity<Object> getItem(Long ownerId, Long itemId) {
        return get(String.format("/%s", itemId), ownerId);
    }

    public ResponseEntity<Object> getItems(Long userid) {
        return get("", userid);
    }

    public ResponseEntity<Object> getItemsByText(Long userId, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> updateItem(Long ownerId, Long itemId, ItemRequestDto item) {
        return patch(String.format("/%s", itemId), ownerId, item);
    }

    public ResponseEntity<Object> createComment(Long bookerId, Long itemId, CommentRequestDto commentDto) {
        return post(String.format("/%s/comment", itemId), bookerId, commentDto);
    }
}
