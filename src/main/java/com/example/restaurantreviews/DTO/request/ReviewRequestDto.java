package com.example.restaurantreviews.dto.request;

public record ReviewRequestDto(
        Long userId,
        Long restaurantId,
        int score,
        String text
) {}