package com.example.restaurantreviews.service;

import com.example.restaurantreviews.dto.request.UserRequestDto;
import com.example.restaurantreviews.dto.response.UserResponseDto;
import com.example.restaurantreviews.entity.User;
import com.example.restaurantreviews.mapper.UserMapper;
import com.example.restaurantreviews.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; // <-- Убедитесь, что этот импорт есть
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto create(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    public UserResponseDto update(Long id, UserRequestDto dto) {
        // Убеждаемся, что пользователь существует, прежде чем обновлять
        userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Пользователь с ID " + id + " не найден"));

        User user = userMapper.toEntity(dto);
        user.setId(id);
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }

    public boolean remove(Long id) {
        return userRepository.remove(id);
    }

    // --- ИСПРАВЛЕННЫЙ МЕТОД ---
    // Теперь он корректно возвращает Optional<UserResponseDto>
    public Optional<UserResponseDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto);
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}