package com.example.restaurantreviews.dto.response;

import com.example.restaurantreviews.enums.CuisineType;
import java.math.BigDecimal;

public record RestaurantResponseDto(
        Long id,
        String name,
        String description,
        CuisineType cuisineType,
        int averageCheck,
        BigDecimal rating
) {}