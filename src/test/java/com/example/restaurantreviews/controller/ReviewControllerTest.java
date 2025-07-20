package com.example.restaurantreviews.controller;

import com.example.restaurantreviews.dto.request.ReviewRequestDto;
import com.example.restaurantreviews.dto.response.ReviewResponseDto;
import com.example.restaurantreviews.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getReviewById_whenFound_shouldReturnReview() throws Exception {
        // Arrange
        long reviewId = 1L;
        ReviewResponseDto responseDto = new ReviewResponseDto(reviewId, 1L, 1L, 5, "Отлично!");
        given(reviewService.findById(reviewId)).willReturn(Optional.of(responseDto));

        // Act & Assert
        mockMvc.perform(get("/api/reviews/{id}", reviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reviewId))
                .andExpect(jsonPath("$.score").value(5));
    }

    @Test
    void createReview_shouldReturnCreatedReview() throws Exception {
        // Arrange
        ReviewRequestDto requestDto = new ReviewRequestDto(1L, 1L, 5, "Отлично!");
        ReviewResponseDto responseDto = new ReviewResponseDto(1L, 1L, 1L, 5, "Отлично!");
        given(reviewService.create(any(ReviewRequestDto.class))).willReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateReview_shouldReturnUpdatedReview() throws Exception {
        // Arrange
        long reviewId = 1L;
        ReviewRequestDto requestDto = new ReviewRequestDto(1L, 1L, 4, "Неплохо");
        ReviewResponseDto responseDto = new ReviewResponseDto(reviewId, 1L, 1L, 4, "Неплохо");
        given(reviewService.update(eq(reviewId), any(ReviewRequestDto.class))).willReturn(responseDto);

        // Act & Assert
        mockMvc.perform(put("/api/reviews/{id}", reviewId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(4));
    }

    @Test
    void deleteReview_shouldReturnNoContent() throws Exception {
        // Arrange
        long reviewId = 1L;
        doNothing().when(reviewService).remove(reviewId);

        // Act & Assert
        mockMvc.perform(delete("/api/reviews/{id}", reviewId))
                .andExpect(status().isNoContent());
    }
}