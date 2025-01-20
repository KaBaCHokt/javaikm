package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Получить всех клиентов
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Сохранить клиента (добавить или обновить)
    public void saveUser(User user) {
        // Проверка имени клиента
        if (user.getFirstName() == null || user.getFirstName().trim().isEmpty()) {
            throw new IllegalArgumentException("Имя клиента не может быть пустым");
        }

        // Проверка фамилии клиента
        if (user.getLastName() == null || user.getLastName().trim().isEmpty()) {
            throw new IllegalArgumentException("Фамилия клиента не может быть пустой");
        }

        // Проверка номера телефона (пример простой проверки)
        if (user.getPhone() == null || !user.getPhone().matches("\\+?\\d{10,15}")) {
            throw new IllegalArgumentException("Номер телефона должен содержать от 10 до 15 цифр");
        }

        // Проверка email (пример простой проверки)
        if (user.getEmail() != null && !user.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Некорректный формат email");
        }

        // Сохранение клиента в репозитории
        userRepository.save(user);
    }

    // Найти клиента по ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Удалить клиента по ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}