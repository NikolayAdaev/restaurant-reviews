package com.example.restaurantreviews.mapper;

import com.example.restaurantreviews.dto.request.ReviewRequestDto;
import com.example.restaurantreviews.dto.response.ReviewResponseDto;
import com.example.restaurantreviews.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    ReviewResponseDto toResponseDto(Review entity);

    Review toEntity(ReviewRequestDto dto);
}