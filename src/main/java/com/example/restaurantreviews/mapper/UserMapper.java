package com.example.restaurantreviews.mapper;

import com.example.restaurantreviews.dto.request.UserRequestDto;
import com.example.restaurantreviews.dto.response.UserResponseDto;
import com.example.restaurantreviews.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto dto);
    UserResponseDto toResponseDto(User entity);
}