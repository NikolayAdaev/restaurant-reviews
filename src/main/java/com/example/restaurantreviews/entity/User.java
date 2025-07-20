package com.example.restaurantreviews.entity;

import com.example.restaurantreviews.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Генерирует геттеры, сеттеры, equals, hashCode, toString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name; // Не обязательное
    private int age;
    private Gender gender;
}