package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserRequestDto;
import ru.practicum.shareit.user.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImplTest {

    private final EntityManager em;
    private final UserService userService;

    @Test
    public void checkSuccessCreateUser() {
        UserRequestDto userRequestDto = prepareUser("Тест", "newTest@test.com");
        userService.createUser(userRequestDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 4L).getSingleResult();

        assertThat(user.getId(), equalTo(4L));
        assertThat(user.getName(), equalTo("Тест"));
        assertThat(user.getEmail(), equalTo("newTest@test.com"));
    }

    @Test
    public void checkSuccessGetUser() {
        UserDto userDto = userService.getUser(1L);

        assertThat(userDto.getId(), equalTo(1L));
        assertThat(userDto.getName(), equalTo("Михаил"));
        assertThat(userDto.getEmail(), equalTo("test@test.ru"));
    }

    @Test
    public void checkSuccessUpdateUser() {
        UserRequestDto userRequestDto = prepareUser("Обновленный", "Серьезно");
        userService.updateUser(2L, userRequestDto);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 2L).getSingleResult();

        assertThat(user.getId(), equalTo(2L));
        assertThat(user.getName(), equalTo(userRequestDto.getName()));
        assertThat(user.getEmail(), equalTo(userRequestDto.getEmail()));
    }

    @Test
    public void checkSuccessDeleteUser() {
        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", 3L).getSingleResult();
        assertThat(user, notNullValue());
        userService.deleteUser(3L);
        int count = query.setParameter("id", 3L).getFirstResult();
        assertThat(count, equalTo(0));
    }

    private UserRequestDto prepareUser(String name, String email) {
        return UserRequestDto.builder()
                .name(name)
                .email(email)
                .build();
    }
}
