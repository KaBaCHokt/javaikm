package com.example.demo.service;

import com.example.demo.entity.Inventory;
import com.example.demo.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional
    public boolean processCarSale(Long carId) {
        // Находим все записи в инвентаре по ID автомобиля, отсортированные по quantity (от меньшего к большему)
        List<Inventory> inventories = inventoryRepository.findByCar_CarIdOrderByQuantityAsc(carId);

        if (inventories.isEmpty()) {
            throw new IllegalArgumentException("Автомобиль не найден");
        }

        // Берем первую запись (с наименьшим количеством)
        Inventory inventoryToUpdate = inventories.get(0);

        // Проверяем количество
        int quantity = inventoryToUpdate.getQuantity();
        if (quantity <= 0) {
            throw new IllegalArgumentException("автомобиля нет на складе");
        }

        // Уменьшаем количество на 1
        inventoryToUpdate.setQuantity(quantity - 1);

        // Если количество стало равно нулю, удаляем запись из инвентаря
        if (inventoryToUpdate.getQuantity() == 0) {
            inventoryRepository.delete(inventoryToUpdate);
        } else {
            inventoryRepository.save(inventoryToUpdate);
        }

        return true;
    }

    // Получить весь инвентарь
    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    // Сохранить запись инвентаря (добавить или обновить)
    public void saveInventory(Inventory inventory) {
        // Проверка количества (должно быть положительным)
        if (inventory.getQuantity() < 0) {
            throw new IllegalArgumentException("Количество должно быть положительным числом");
        }

        inventoryRepository.save(inventory);
    }

    // Найти запись инвентаря по ID
    public Inventory getInventoryById(Long id) {
        return inventoryRepository.findById(id).orElse(null);
    }

    // Удалить запись инвентаря по ID
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}