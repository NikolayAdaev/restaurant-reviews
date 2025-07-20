package com.example.restaurantreviews.repository;

import com.example.restaurantreviews.entity.Restaurant;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    public Restaurant save(Restaurant restaurant) {
        if (restaurant.getId() == null) {
            restaurant.setId(idCounter.incrementAndGet());
        } else {
            restaurants.removeIf(r -> r.getId().equals(restaurant.getId()));
        }
        restaurants.add(restaurant);
        return restaurant;
    }

    public boolean remove(Long id) {
        return restaurants.removeIf(r -> r.getId().equals(id));
    }

    public List<Restaurant> findAll() {
        return new ArrayList<>(restaurants);
    }

    // Добавим метод findById, он пригодится в сервисе
    public Optional<Restaurant> findById(Long id) {
        return restaurants.stream().filter(r -> r.getId().equals(id)).findFirst();
    }
}