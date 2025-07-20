package com.example.restaurantreviews.dto.response;

import com.example.restaurantreviews.enums.Gender;

public record UserResponseDto(
        Long id,
        String name,
        int age,
        Gender gender
) {}