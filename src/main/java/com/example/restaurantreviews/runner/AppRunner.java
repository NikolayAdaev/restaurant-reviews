package com.example.restaurantreviews.runner;

import com.example.restaurantreviews.dto.request.RestaurantRequestDto;
import com.example.restaurantreviews.dto.request.ReviewRequestDto;
import com.example.restaurantreviews.dto.request.UserRequestDto;
import com.example.restaurantreviews.dto.response.RestaurantResponseDto;
import com.example.restaurantreviews.dto.response.UserResponseDto;
import com.example.restaurantreviews.enums.CuisineType;
import com.example.restaurantreviews.enums.Gender;
import com.example.restaurantreviews.service.RestaurantService;
import com.example.restaurantreviews.service.ReviewService;
import com.example.restaurantreviews.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    // --- ПОЛНОСТЬЮ ПЕРЕПИСАН МЕТОД INIT ---
    @PostConstruct
    public void init() {
        // Проверяем, есть ли уже данные, чтобы не создавать их повторно при перезапуске
        if (userService.findAll().isEmpty()) {
            System.out.println(">>> База данных пуста. Заполняем начальными данными...");

            // Создаем DTO для пользователей
            UserRequestDto user1Dto = new UserRequestDto("Иван", 30, Gender.MALE);
            UserRequestDto user2Dto = new UserRequestDto("Мария", 25, Gender.FEMALE);

            // Сохраняем и получаем ответ с ID
            UserResponseDto user1 = userService.create(user1Dto);
            UserResponseDto user2 = userService.create(user2Dto);

            // Создаем DTO для ресторанов
            RestaurantRequestDto r1Dto = new RestaurantRequestDto("Марио", "Лучшая пицца в городе", CuisineType.ITALIAN, 1500);
            RestaurantRequestDto r2Dto = new RestaurantRequestDto("Золотой Дракон", null, CuisineType.CHINESE, 2000);

            // Сохраняем и получаем ответ с ID
            RestaurantResponseDto r1 = restaurantService.create(r1Dto);
            RestaurantResponseDto r2 = restaurantService.create(r2Dto);

            // Создаем DTO для отзывов, используя полученные ID
            reviewService.create(new ReviewRequestDto(user1.id(), r1.id(), 5, "Отличная пицца!"));
            reviewService.create(new ReviewRequestDto(user2.id(), r1.id(), 4, "Хорошее место."));
            reviewService.create(new ReviewRequestDto(user1.id(), r2.id(), 3, "Еда на троечку."));

            System.out.println(">>> Начальные данные успешно созданы!");
        }
    }

    // --- ОБНОВЛЕН МЕТОД RUN ---
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=======================================================================");
        System.out.println("===== Приложение запущено. API доступно по адресу http://localhost:8080 =====");
        System.out.println("===== Документация Swagger UI: http://localhost:8080/swagger-ui.html =====");
        System.out.println("=======================================================================");
    }
}