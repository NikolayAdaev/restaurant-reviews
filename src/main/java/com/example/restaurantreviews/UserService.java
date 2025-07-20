package com.example.restaurantreviews;

import com.example.restaurantreviews.dto.request.UserRequestDto;
import com.example.restaurantreviews.dto.response.UserResponseDto;
import com.example.restaurantreviews.entity.User;
import com.example.restaurantreviews.mapper.UserMapper;
import com.example.restaurantreviews.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto create(UserRequestDto dto) {
        User user = userMapper.toEntity(dto);
        return userMapper.toResponseDto(userRepository.save(user));
    }

    public UserResponseDto update(Long id, UserRequestDto dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setName(dto.name());
        existingUser.setAge(dto.age());
        existingUser.setGender(dto.gender());

        return userMapper.toResponseDto(userRepository.save(existingUser));
    }

    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDto);
    }
}