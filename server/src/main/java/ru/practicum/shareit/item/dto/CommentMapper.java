package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static CommentDto mapToCommentDto(Comment comment) {
        log.info("Comment в маппер: {}", comment);
        CommentDto commentDto = CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
        log.info("CommentDto из мапера: {}", commentDto);
        return commentDto;
    }

    public static Comment mapToComment(CommentRequestDto commentDto, Item item, User user) {
        log.info("CommentDto в маппер: {}", commentDto);
        Comment comment = Comment.builder()
                .text(commentDto.getText())
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
        log.info("Comment из маппера: {}", comment);
        return comment;
    }

    public static List<CommentDto> mapToCommentDto(List<Comment> comments) {
        log.info("Comments в маппер: {}", comments);
        List<CommentDto> commentDtos = comments.stream()
                .map(CommentMapper::mapToCommentDto)
                .toList();
        log.info("CommentDtos из маппера: {}", commentDtos);
        return commentDtos;
    }
}
