package com.example.restaurantreviews.dto.request;

import com.example.restaurantreviews.enums.CuisineType;

public record RestaurantRequestDto(
        String name,
        String description,
        CuisineType cuisineType,
        int averageCheck
) {}