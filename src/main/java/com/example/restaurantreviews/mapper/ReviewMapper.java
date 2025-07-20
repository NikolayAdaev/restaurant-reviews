package com.example.restaurantreviews.mapper;

import com.example.restaurantreviews.dto.request.ReviewRequestDto;
import com.example.restaurantreviews.dto.response.ReviewResponseDto;
import com.example.restaurantreviews.entity.Review;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toEntity(ReviewRequestDto dto);
    ReviewResponseDto toResponseDto(Review entity);
}