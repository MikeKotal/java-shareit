package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.user.model.User;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static UserDto mapToUserDto(User user) {
        log.info("User в маппер: {}", user);
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();
        log.info("UserDto из маппера: {}", userDto);
        return userDto;
    }

    public static User mapToUser(UserRequestDto userDto) {
        log.info("UserDto в маппер: {}", userDto);
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
        log.info("User из маппера: {}", user);
        return user;
    }
}
