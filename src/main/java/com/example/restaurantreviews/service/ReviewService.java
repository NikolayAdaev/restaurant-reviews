package com.example.restaurantreviews.service;

import com.example.restaurantreviews.dto.request.ReviewRequestDto;
import com.example.restaurantreviews.dto.response.ReviewResponseDto;
import com.example.restaurantreviews.entity.Restaurant;
import com.example.restaurantreviews.entity.Review;
import com.example.restaurantreviews.entity.User;
import com.example.restaurantreviews.mapper.ReviewMapper;
import com.example.restaurantreviews.repository.RestaurantRepository;
import com.example.restaurantreviews.repository.ReviewRepository;
import com.example.restaurantreviews.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponseDto create(ReviewRequestDto dto) {
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.userId()));
        Restaurant restaurant = restaurantRepository.findById(dto.restaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + dto.restaurantId()));

        Review review = reviewMapper.toEntity(dto);
        review.setUser(user);
        review.setRestaurant(restaurant);

        Review savedReview = reviewRepository.save(review);
        updateRestaurantRating(restaurant.getId());
        return reviewMapper.toResponseDto(savedReview);
    }

    @Transactional
    public ReviewResponseDto update(Long id, ReviewRequestDto dto) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));

        existingReview.setScore(dto.score());
        existingReview.setText(dto.text());

        Review updatedReview = reviewRepository.save(existingReview);

        // Пересчитываем рейтинг после обновления оценки
        updateRestaurantRating(updatedReview.getRestaurant().getId());

        return reviewMapper.toResponseDto(updatedReview);
    }

    @Transactional
    public void remove(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        Long restaurantId = review.getRestaurant().getId();
        reviewRepository.deleteById(id);
        updateRestaurantRating(restaurantId);
    }

    public List<ReviewResponseDto> findAll() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<ReviewResponseDto> findById(Long id) {
        return reviewRepository.findById(id).map(reviewMapper::toResponseDto);
    }

    private void updateRestaurantRating(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found while updating rating: " + restaurantId));

        if (reviews.isEmpty()) {
            restaurant.setRating(BigDecimal.ZERO);
        } else {
            double averageScore = reviews.stream()
                    .mapToInt(Review::getScore)
                    .average()
                    .orElse(0.0);
            restaurant.setRating(BigDecimal.valueOf(averageScore).setScale(2, RoundingMode.HALF_UP));
        }
        restaurantRepository.save(restaurant);
    }
}