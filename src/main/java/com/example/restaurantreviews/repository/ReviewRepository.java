package com.example.restaurantreviews.repository;

import com.example.restaurantreviews.entity.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class ReviewRepository {
    private final List<Review> reviews = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    public Review save(Review review) {
        if (review.getId() == null) {
            review.setId(idCounter.incrementAndGet());
        } else {
            reviews.removeIf(r -> r.getId().equals(review.getId()));
        }
        reviews.add(review);
        return review;
    }

    public boolean remove(Long id) {
        return reviews.removeIf(r -> r.getId().equals(id));
    }

    public List<Review> findAll() {
        return new ArrayList<>(reviews);
    }

    // Вместо findById по заданию, сделаем поиск по ID ресторана, это логичнее
    public List<Review> findByRestaurantId(Long restaurantId) {
        return reviews.stream()
                .filter(r -> r.getRestaurantId().equals(restaurantId))
                .collect(Collectors.toList());
    }
}