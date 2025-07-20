package com.example.restaurantreviews.service;

import com.example.restaurantreviews.dto.request.ReviewRequestDto;
import com.example.restaurantreviews.dto.response.ReviewResponseDto;
import com.example.restaurantreviews.entity.Restaurant;
import com.example.restaurantreviews.entity.Review;
import com.example.restaurantreviews.entity.User;
import com.example.restaurantreviews.enums.CuisineType;
import com.example.restaurantreviews.enums.Gender;
import com.example.restaurantreviews.mapper.ReviewMapper;
import com.example.restaurantreviews.repository.RestaurantRepository;
import com.example.restaurantreviews.repository.ReviewRepository;
import com.example.restaurantreviews.repository.UserRepository;
import com.example.restaurantreviews.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void create_shouldSaveReviewAndUpdateRestaurantRating() {
        // --- Arrange ---
        long userId = 1L;
        long restaurantId = 1L;
        ReviewRequestDto requestDto = new ReviewRequestDto(userId, restaurantId, 5, "Отлично!");

        User user = new User(userId, "Тест", 20, Gender.MALE, null);
        Restaurant restaurant = new Restaurant(restaurantId, "Тестовый Ресторан", "", CuisineType.EUROPEAN, 1000, BigDecimal.ZERO, null);
        Review reviewToSave = new Review(null, user, restaurant, 5, "Отлично!");
        Review savedReview = new Review(1L, user, restaurant, 5, "Отлично!");
        ReviewResponseDto expectedDto = new ReviewResponseDto(1L, userId, restaurantId, 5, "Отлично!");

        // Настройка поведения моков
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(reviewMapper.toEntity(requestDto)).thenReturn(reviewToSave);
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);
        when(reviewMapper.toResponseDto(savedReview)).thenReturn(expectedDto);

        // Мокирование для логики обновления рейтинга
        Review otherReview = new Review(2L, user, restaurant, 3, "Нормально");
        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(List.of(savedReview, otherReview));

        // --- Act ---
        ReviewResponseDto actualDto = reviewService.create(requestDto);

        // --- Assert ---
        assertThat(actualDto).isEqualTo(expectedDto);

        verify(restaurantRepository, times(1)).save(restaurant);
        assertThat(restaurant.getRating()).isEqualTo(new BigDecimal("4.00"));
    }

    @Test
    void remove_shouldDeleteReviewAndUpdateRatingToZero() {
        // Arrange
        long reviewId = 1L;
        long restaurantId = 1L;

        User user = new User(1L, "Тест", 20, Gender.MALE, null);
        Restaurant restaurant = new Restaurant(restaurantId, "Тестовый Ресторан", "", CuisineType.EUROPEAN, 1000, new BigDecimal("5.00"), null);
        Review reviewToDelete = new Review(reviewId, user, restaurant, 5, "Отлично!");

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(reviewToDelete));
        doNothing().when(reviewRepository).deleteById(reviewId);
        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(Collections.emptyList());
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        // Act
        reviewService.remove(reviewId);

        // Assert
        verify(reviewRepository, times(1)).deleteById(reviewId);
        verify(restaurantRepository, times(1)).save(restaurant);
        assertThat(restaurant.getRating()).isEqualTo(BigDecimal.ZERO);
    }
}