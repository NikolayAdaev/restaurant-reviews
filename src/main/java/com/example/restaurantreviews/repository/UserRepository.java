package com.example.restaurantreviews.repository;

import com.example.restaurantreviews.entity.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(idCounter.incrementAndGet());
        } else {
            // Удаляем старую версию, если она есть (для обновления)
            users.removeIf(u -> u.getId().equals(user.getId()));
        }
        users.add(user);
        return user;
    }

    public boolean remove(Long id) {
        return users.removeIf(u -> u.getId().equals(id));
    }

    public List<User> findAll() {
        return new ArrayList<>(users);
    }
}