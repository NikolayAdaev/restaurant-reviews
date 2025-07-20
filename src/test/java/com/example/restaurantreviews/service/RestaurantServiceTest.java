package com.example.restaurantreviews.service;

import com.example.restaurantreviews.dto.request.RestaurantRequestDto;
import com.example.restaurantreviews.dto.response.RestaurantResponseDto;
import com.example.restaurantreviews.entity.Restaurant;
import com.example.restaurantreviews.enums.CuisineType;
import com.example.restaurantreviews.mapper.RestaurantMapper;
import com.example.restaurantreviews.repository.RestaurantRepository;
import com.example.restaurantreviews.RestaurantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    @Test
    void findAll_shouldReturnListOfRestaurantDtos() {
        // Arrange
        Restaurant restaurant1 = new Restaurant(1L, "Марио", "Пиццерия", CuisineType.ITALIAN, 1500, new BigDecimal("4.5"), null);
        RestaurantResponseDto dto1 = new RestaurantResponseDto(1L, "Марио", "Пиццерия", CuisineType.ITALIAN, 1500, new BigDecimal("4.5"));
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant1));
        when(restaurantMapper.toResponseDto(restaurant1)).thenReturn(dto1);

        // Act
        List<RestaurantResponseDto> result = restaurantService.findAll();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Марио");
        verify(restaurantRepository, times(1)).findAll();
    }

    @Test
    void create_shouldSaveRestaurantAndReturnDto() {
        // Arrange
        RestaurantRequestDto requestDto = new RestaurantRequestDto("Золотой Дракон", "Китайская кухня", CuisineType.CHINESE, 2000);
        Restaurant restaurantToSave = new Restaurant(null, "Золотой Дракон", "Китайская кухня", CuisineType.CHINESE, 2000, BigDecimal.ZERO, null);
        Restaurant savedRestaurant = new Restaurant(1L, "Золотой Дракон", "Китайская кухня", CuisineType.CHINESE, 2000, BigDecimal.ZERO, null);
        RestaurantResponseDto expectedDto = new RestaurantResponseDto(1L, "Золотой Дракон", "Китайская кухня", CuisineType.CHINESE, 2000, BigDecimal.ZERO);

        when(restaurantMapper.toEntity(requestDto)).thenReturn(restaurantToSave);
        when(restaurantRepository.save(restaurantToSave)).thenReturn(savedRestaurant);
        when(restaurantMapper.toResponseDto(savedRestaurant)).thenReturn(expectedDto);

        // Act
        RestaurantResponseDto actualDto = restaurantService.create(requestDto);

        // Assert
        assertThat(actualDto).isEqualTo(expectedDto);
        verify(restaurantRepository, times(1)).save(restaurantToSave);
    }

    @Test
    void remove_shouldCallDeleteById() {
        // Arrange
        long restaurantId = 1L;
        doNothing().when(restaurantRepository).deleteById(restaurantId);

        // Act
        restaurantService.remove(restaurantId);

        // Assert
        verify(restaurantRepository, times(1)).deleteById(restaurantId);
    }
}