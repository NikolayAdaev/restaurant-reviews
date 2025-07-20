package com.example.restaurantreviews.controller;

import com.example.restaurantreviews.dto.request.UserRequestDto;
import com.example.restaurantreviews.dto.response.UserResponseDto;
import com.example.restaurantreviews.enums.Gender;
import com.example.restaurantreviews.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllUsers_shouldReturnUserList() throws Exception {
        // --- Arrange ---
        UserResponseDto user1 = new UserResponseDto(1L, "Иван", 30, Gender.MALE);
        given(userService.findAll()).willReturn(List.of(user1));

        // --- Act & Assert ---
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Иван"));
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        // --- Arrange ---
        UserRequestDto requestDto = new UserRequestDto("Мария", 25, Gender.FEMALE);
        UserResponseDto responseDto = new UserResponseDto(1L, "Мария", 25, Gender.FEMALE);

        given(userService.create(any(UserRequestDto.class))).willReturn(responseDto);

        // --- Act & Assert ---
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Мария"));
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        // --- Arrange ---
        long userId = 1L;
        UserRequestDto requestDto = new UserRequestDto("Иван Петров", 31, Gender.MALE);
        UserResponseDto responseDto = new UserResponseDto(userId, "Иван Петров", 31, Gender.MALE);

        given(userService.update(eq(userId), any(UserRequestDto.class))).willReturn(responseDto);

        // --- Act & Assert ---
        mockMvc.perform(put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Иван Петров"))
                .andExpect(jsonPath("$.age").value(31));
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        // --- Arrange ---
        long userId = 1L;
        doNothing().when(userService).remove(userId);

        // --- Act & Assert ---
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());
    }
}