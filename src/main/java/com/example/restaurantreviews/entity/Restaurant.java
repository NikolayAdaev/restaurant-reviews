package com.example.restaurantreviews.entity;

import com.example.restaurantreviews.enums.CuisineType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {
    private Long id;
    private String name;
    private String description; // Не обязательное
    private CuisineType cuisineType;
    private int averageCheck;
    private BigDecimal rating; // Средняя оценка
}