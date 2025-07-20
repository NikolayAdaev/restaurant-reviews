package com.example.restaurantreviews.dto.response;

public record ReviewResponseDto(
        Long id,
        Long userId,
        Long restaurantId,
        int score,
        String text
) {}