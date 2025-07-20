package com.example.restaurantreviews.service;

import com.example.restaurantreviews.entity.User;
import com.example.restaurantreviews.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // Создает конструктор для final полей
public class UserService {
    private final UserRepository userRepository;

    public User save(User user) {
        // Здесь могла бы быть логика, например, проверка возраста
        return userRepository.save(user);
    }

    public boolean remove(Long id) {
        return userRepository.remove(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}