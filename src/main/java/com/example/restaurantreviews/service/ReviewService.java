package com.example.restaurantreviews.service;

import com.example.restaurantreviews.dto.request.ReviewRequestDto;
import com.example.restaurantreviews.dto.response.ReviewResponseDto;
import com.example.restaurantreviews.entity.Restaurant;
import com.example.restaurantreviews.entity.Review;
import com.example.restaurantreviews.mapper.ReviewMapper;
import com.example.restaurantreviews.repository.RestaurantRepository;
import com.example.restaurantreviews.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final ReviewMapper reviewMapper;

    public ReviewResponseDto create(ReviewRequestDto dto) {
        Review review = reviewMapper.toEntity(dto);
        Review savedReview = reviewRepository.save(review);
        updateRestaurantRating(savedReview.getRestaurantId());
        return reviewMapper.toResponseDto(savedReview);
    }

    public ReviewResponseDto update(Long id, ReviewRequestDto dto) {
        Review review = reviewMapper.toEntity(dto);
        review.setId(id);
        Review updatedReview = reviewRepository.save(review);
        updateRestaurantRating(updatedReview.getRestaurantId());
        return reviewMapper.toResponseDto(updatedReview);
    }

    public boolean remove(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isEmpty()) {
            return false;
        }

        Long restaurantId = reviewOptional.get().getRestaurantId();
        boolean removed = reviewRepository.remove(id);

        if (removed) {
            updateRestaurantRating(restaurantId);
        }
        return removed;
    }

    public List<ReviewResponseDto> findAll() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<ReviewResponseDto> findById(Long id) {
        return reviewRepository.findById(id)
                .map(reviewMapper::toResponseDto);
    }

    private void updateRestaurantRating(Long restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalStateException("Ресторан с ID " + restaurantId + " не найден"));

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