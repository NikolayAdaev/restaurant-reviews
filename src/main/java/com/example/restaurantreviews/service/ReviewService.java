package com.example.restaurantreviews.service;

import com.example.restaurantreviews.entity.Restaurant;
import com.example.restaurantreviews.entity.Review;
import com.example.restaurantreviews.repository.RestaurantRepository;
import com.example.restaurantreviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    // Нужен доступ к репозиторию ресторанов, чтобы обновлять рейтинг
    private final RestaurantRepository restaurantRepository;

    public Review save(Review review) {
        reviewRepository.save(review);
        // После сохранения отзыва пересчитываем рейтинг ресторана
        updateRestaurantRating(review.getRestaurantId());
        return review;
    }

    public boolean remove(Long reviewId) {
        // Перед удалением отзыва нам нужно знать, к какому ресторану он относится
        Long restaurantId = reviewRepository.findAll().stream()
                .filter(r -> r.getId().equals(reviewId))
                .findFirst()
                .map(Review::getRestaurantId)
                .orElse(null);

        boolean removed = reviewRepository.remove(reviewId);
        if (removed && restaurantId != null) {
            // Если удаление прошло успешно, пересчитываем рейтинг
            updateRestaurantRating(restaurantId);
        }
        return removed;
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    // Приватный метод для основной бизнес-логики
    private void updateRestaurantRating(Long restaurantId) {
        // Находим все отзывы для данного ресторана
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);

        // Находим сам ресторан
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalStateException("Ресторан с ID " + restaurantId + " не найден"));

        if (reviews.isEmpty()) {
            restaurant.setRating(BigDecimal.ZERO);
        } else {
            // Считаем среднее арифметическое
            double averageScore = reviews.stream()
                    .mapToInt(Review::getScore)
                    .average()
                    .orElse(0.0);

            // Устанавливаем новый рейтинг с округлением до 2 знаков после запятой
            restaurant.setRating(BigDecimal.valueOf(averageScore).setScale(2, RoundingMode.HALF_UP));
        }

        // Сохраняем обновленный ресторан
        restaurantRepository.save(restaurant);
    }
}