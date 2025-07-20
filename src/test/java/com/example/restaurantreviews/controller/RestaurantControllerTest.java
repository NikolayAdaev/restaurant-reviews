package com.example.restaurantreviews.controller;

import com.example.restaurantreviews.dto.request.RestaurantRequestDto;
import com.example.restaurantreviews.dto.response.RestaurantResponseDto;
import com.example.restaurantreviews.enums.CuisineType;
import com.example.restaurantreviews.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getRestaurantById_whenFound_shouldReturnRestaurant() throws Exception {
        // Arrange
        long restaurantId = 1L;
        RestaurantResponseDto responseDto = new RestaurantResponseDto(restaurantId, "Марио", "Пиццерия", CuisineType.ITALIAN, 1500, new BigDecimal("4.50"));
        given(restaurantService.findById(restaurantId)).willReturn(Optional.of(responseDto));

        // Act & Assert
        mockMvc.perform(get("/api/restaurants/{id}", restaurantId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(restaurantId))
                .andExpect(jsonPath("$.name").value("Марио"));
    }

    @Test
    void getRestaurantById_whenNotFound_shouldReturn404() throws Exception {
        // Arrange
        long restaurantId = 99L;
        given(restaurantService.findById(restaurantId)).willReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/restaurants/{id}", restaurantId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRestaurant_shouldReturnCreatedRestaurant() throws Exception {
        // Arrange
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Новый Ресторан", null, CuisineType.EUROPEAN, 2000);
        RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Новый Ресторан", null, CuisineType.EUROPEAN, 2000, BigDecimal.ZERO);

        given(restaurantService.create(any(RestaurantRequestDto.class))).willReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteRestaurant_shouldReturnNoContent() throws Exception {
        // Arrange
        long restaurantId = 1L;
        doNothing().when(restaurantService).remove(restaurantId);

        // Act & Assert
        mockMvc.perform(delete("/api/restaurants/{id}", restaurantId))
                .andExpect(status().isNoContent());
    }
}