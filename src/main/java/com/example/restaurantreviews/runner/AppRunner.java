package com.example.restaurantreviews.runner;

import com.example.restaurantreviews.entity.Restaurant;
import com.example.restaurantreviews.entity.Review;
import com.example.restaurantreviews.entity.User;
import com.example.restaurantreviews.enums.CuisineType;
import com.example.restaurantreviews.enums.Gender;
import com.example.restaurantreviews.service.RestaurantService;
import com.example.restaurantreviews.service.ReviewService;
import com.example.restaurantreviews.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final UserService userService;
    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    // Этот блок выполнится один раз после создания бина
    // и заполнит наши "базы данных" начальными данными
    @PostConstruct
    public void init() {
        // Создаем пользователей
        User user1 = userService.save(new User(null, "Иван", 30, Gender.MALE));
        User user2 = userService.save(new User(null, "Мария", 25, Gender.FEMALE));
        userService.save(new User(null, null, 42, Gender.MALE)); // Анонимный

        // Создаем рестораны
        Restaurant r1 = restaurantService.save(new Restaurant(null, "Марио", "Лучшая пицца в городе", CuisineType.ITALIAN, 1500, BigDecimal.ZERO));
        Restaurant r2 = restaurantService.save(new Restaurant(null, "Золотой Дракон", null, CuisineType.CHINESE, 2000, BigDecimal.ZERO));

        // Добавляем оценки
        reviewService.save(new Review(null, user1.getId(), r1.getId(), 5, "Отличная пицца, рекомендую!"));
        reviewService.save(new Review(null, user2.getId(), r1.getId(), 4, "Хорошее место, но долго ждали."));
        reviewService.save(new Review(null, user1.getId(), r2.getId(), 3, "Еда на троечку."));
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("===== Приложение запущено и готово к демонстрации =====");

        // 1. Посмотрим на все рестораны и их рейтинги после инициализации
        System.out.println("\n--- Начальное состояние ресторанов:");
        restaurantService.findAll().forEach(System.out::println);

        // 2. Добавим новый отзыв и посмотрим, как изменится рейтинг
        System.out.println("\n--- Добавляем новый отзыв для ресторана 'Золотой Дракон'...");
        User ananim = userService.findAll().stream().filter(u -> u.getName() == null).findFirst().get();
        reviewService.save(new Review(null, ananim.getId(), 2L, 5, "Внезапно, очень понравилось!"));

        System.out.println("\n--- Состояние ресторанов после нового отзыва:");
        restaurantService.findAll().forEach(System.out::println);

        // 3. Удалим один из отзывов
        System.out.println("\n--- Удаляем первый отзыв для ресторана 'Марио' (ID=1)...");
        reviewService.remove(1L);

        System.out.println("\n--- Состояние ресторанов после удаления отзыва:");
        restaurantService.findAll().forEach(System.out::println);

        System.out.println("\n===== Демонстрация завершена =====");
    }
}