package com.example.restaurantreviews.service;

import com.example.restaurantreviews.dto.request.UserRequestDto;
import com.example.restaurantreviews.dto.response.UserResponseDto;
import com.example.restaurantreviews.entity.User;
import com.example.restaurantreviews.enums.Gender;
import com.example.restaurantreviews.mapper.UserMapper;
import com.example.restaurantreviews.repository.UserRepository;
import com.example.restaurantreviews.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// Включаем поддержку Mockito в JUnit 5
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;


    @InjectMocks
    private UserService userService;

    @Test
    void findById_shouldReturnUser_whenUserExists() {
        long userId = 1L;
        User userEntity = new User(userId, "Иван", 30, Gender.MALE, null);
        UserResponseDto expectedDto = new UserResponseDto(userId, "Иван", 30, Gender.MALE);


        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponseDto(userEntity)).thenReturn(expectedDto);

        // --- Act ---
        Optional<UserResponseDto> actualResult = userService.findById(userId);

        // --- Assert ---
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(expectedDto);


        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).toResponseDto(userEntity);
    }

    @Test
    void create_shouldSaveAndReturnUser() {
        // --- Arrange ---
        UserRequestDto requestDto = new UserRequestDto("Мария", 25, Gender.FEMALE);
        User userToSave = new User(null, "Мария", 25, Gender.FEMALE, null);
        User savedUser = new User(1L, "Мария", 25, Gender.FEMALE, null);
        UserResponseDto expectedDto = new UserResponseDto(1L, "Мария", 25, Gender.FEMALE);

        when(userMapper.toEntity(requestDto)).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(savedUser);
        when(userMapper.toResponseDto(savedUser)).thenReturn(expectedDto);

        // --- Act ---
        UserResponseDto actualDto = userService.create(requestDto);

        // --- Assert ---
        assertThat(actualDto).isNotNull();
        assertThat(actualDto.id()).isEqualTo(1L);
        assertThat(actualDto.name()).isEqualTo("Мария");

        verify(userRepository, times(1)).save(userToSave);
    }
}