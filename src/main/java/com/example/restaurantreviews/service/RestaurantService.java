package com.example.restaurantreviews.service;

import com.example.restaurantreviews.dto.request.RestaurantRequestDto;
import com.example.restaurantreviews.dto.response.RestaurantResponseDto;
import com.example.restaurantreviews.entity.Restaurant;
import com.example.restaurantreviews.mapper.RestaurantMapper;
import com.example.restaurantreviews.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    public RestaurantResponseDto create(RestaurantRequestDto dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toResponseDto(savedRestaurant);
    }

    public RestaurantResponseDto update(Long id, RestaurantRequestDto dto) {
        // Находим существующий ресторан, чтобы не потерять его рейтинг
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Ресторан с ID " + id + " не найден"));

        // Обновляем поля из DTO
        existingRestaurant.setName(dto.name());
        existingRestaurant.setDescription(dto.description());
        existingRestaurant.setCuisineType(dto.cuisineType());
        existingRestaurant.setAverageCheck(dto.averageCheck());

        Restaurant updatedRestaurant = restaurantRepository.save(existingRestaurant);
        return restaurantMapper.toResponseDto(updatedRestaurant);
    }

    public boolean remove(Long id) {
        return restaurantRepository.remove(id);
    }

    public List<RestaurantResponseDto> findAll() {
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<RestaurantResponseDto> findById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantMapper::toResponseDto);
    }
}