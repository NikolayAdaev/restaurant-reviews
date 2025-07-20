package com.example.restaurantreviews;

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
        return restaurantMapper.toResponseDto(restaurantRepository.save(restaurant));
    }

    public RestaurantResponseDto update(Long id, RestaurantRequestDto dto) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));

        existingRestaurant.setName(dto.name());
        existingRestaurant.setDescription(dto.description());
        existingRestaurant.setCuisineType(dto.cuisineType());
        existingRestaurant.setAverageCheck(dto.averageCheck());

        return restaurantMapper.toResponseDto(restaurantRepository.save(existingRestaurant));
    }

    public void remove(Long id) {
        restaurantRepository.deleteById(id);
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