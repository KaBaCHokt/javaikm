package com.example.demo.service;

import com.example.demo.entity.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    // Получить все автомобили
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // Сохранить автомобиль (добавить или обновить)
    public void saveCar(Car car) {
        // Проверка года выпуска (должен быть в диапазоне 1900–2023)
        if (car.getYear() < 1900 || car.getYear() > 2025) {
            throw new IllegalArgumentException("Год должен быть в диапазоне от 1900 до 2023");
        }

        // Проверка цены (должна быть положительной)
        if (car.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательным числом");
        }

        carRepository.save(car);
    }

    // Найти автомобиль по ID
    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public boolean isCarAvailable(Long carId) {
        return carRepository.existsById(carId);
    }

    // Удалить автомобиль по ID
    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}