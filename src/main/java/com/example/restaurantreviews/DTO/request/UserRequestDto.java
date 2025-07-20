package com.example.restaurantreviews.dto.request;

import com.example.restaurantreviews.enums.Gender;

// Используем record для создания неизменяемого DTO
public record UserRequestDto(
        String name,
        int age,
        Gender gender
) {}