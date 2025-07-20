package com.example.restaurantreviews.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    // Я добавлю ID для самой оценки, это более правильный подход
    private Long id;
    private Long userId;
    private Long restaurantId;
    private int score; // Оценка от 1 до 5
    private String text; // Не обязательное
}