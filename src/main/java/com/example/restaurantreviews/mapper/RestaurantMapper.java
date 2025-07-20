package com.example.restaurantreviews.mapper;

import com.example.restaurantreviews.dto.request.RestaurantRequestDto;
import com.example.restaurantreviews.dto.response.RestaurantResponseDto;
import com.example.restaurantreviews.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "rating", expression = "java(new java.math.BigDecimal(0))")
    Restaurant toEntity(RestaurantRequestDto dto);
    RestaurantResponseDto toResponseDto(Restaurant entity);
}