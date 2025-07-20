package com.example.restaurantreviews.controller;

import com.example.restaurantreviews.dto.request.UserRequestDto;
import com.example.restaurantreviews.dto.response.UserResponseDto;
import com.example.restaurantreviews.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "API для работы с посетителями")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Получить список всех посетителей")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // --- УБЕДИТЕСЬ, ЧТО МЕТОД ВЫГЛЯДИТ ТАК ---
    @GetMapping("/{id}")
    @Operation(summary = "Получить посетителя по ID")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать нового посетителя")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto dto) {
        return new ResponseEntity<>(userService.create(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить данные посетителя")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserRequestDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить посетителя")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        return userService.remove(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}