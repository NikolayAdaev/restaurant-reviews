package com.example.restaurantreviews.controller;

import com.example.restaurantreviews.dto.request.RestaurantRequestDto;
import com.example.restaurantreviews.dto.response.RestaurantResponseDto;
import com.example.restaurantreviews.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant Controller", description = "API для работы с ресторанами")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "Получить список всех ресторанов")
    public ResponseEntity<List<RestaurantResponseDto>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить ресторан по ID")
    public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable Long id) {
        return restaurantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новый ресторан")
    public ResponseEntity<RestaurantResponseDto> createRestaurant(@RequestBody RestaurantRequestDto dto) {
        return new ResponseEntity<>(restaurantService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующий ресторан")
    public ResponseEntity<RestaurantResponseDto> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantRequestDto dto) {
        return ResponseEntity.ok(restaurantService.update(id, dto));
    }

    // --- ИСПРАВЛЕН МЕТОД ---
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить ресторан")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantService.remove(id);
        return ResponseEntity.noContent().build();
    }
}