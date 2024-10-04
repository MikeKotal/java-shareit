package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.TestData.USER_DTO;
import static ru.practicum.shareit.TestData.USER_REQUEST_DTO;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void whenCallCreateUserThenReturnNewUser() throws Exception {
        when(userService.createUser(USER_REQUEST_DTO))
                .thenReturn(USER_DTO);

        mockMvc.perform(post("/users")
                        .content(mapper.writeValueAsString(USER_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(USER_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(USER_DTO.getName()), String.class))
                .andExpect(jsonPath("$.email", is(USER_DTO.getEmail()), String.class));
    }

    @Test
    public void whenCallGetUserByIdThenReturnUser() throws Exception {
        when(userService.getUser(1L))
                .thenReturn(USER_DTO);

        mockMvc.perform(get(String.format("/users/%s", "1"))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(USER_DTO.getName()), String.class))
                .andExpect(jsonPath("$.email", is(USER_DTO.getEmail()), String.class));
    }

    @Test
    public void whenCallUpdateUserThenReturnUpdatedUser() throws Exception {
        when(userService.updateUser(1L, USER_REQUEST_DTO))
                .thenReturn(USER_DTO);

        mockMvc.perform(patch(String.format("/users/%s", "1"))
                        .content(mapper.writeValueAsString(USER_REQUEST_DTO))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(USER_DTO.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(USER_DTO.getName()), String.class))
                .andExpect(jsonPath("$.email", is(USER_DTO.getEmail()), String.class));
    }

    @Test
    public void whenCallDeleteUserThenReturn204HttpCode() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete(String.format("/users/%s", 1L))
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
