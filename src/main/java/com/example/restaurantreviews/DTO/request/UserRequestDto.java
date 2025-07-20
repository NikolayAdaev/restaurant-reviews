package com.example.restaurantreviews.dto.request;

import com.example.restaurantreviews.enums.Gender;

public record UserRequestDto(
        String name,
        int age,
        Gender gender
) {}