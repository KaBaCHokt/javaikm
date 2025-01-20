package com.example.demo.service;

import com.example.demo.entity.Sale;
import com.example.demo.repository.SaleRepository;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class SaleService {
    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    // Регулярное выражение для проверки имени клиента
    private static final String NAME_REGEX = "^[A-Za-zА-Яа-я\\s]+$";

    // Получить все продажи
    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    // Сохранить продажу (добавить или обновить)
    public void saveSale(Sale sale) {
        // Проверка имени клиента
        if (!Pattern.matches(NAME_REGEX, sale.getCustomerName())) {
            throw new IllegalArgumentException("Некорректный формат имени клиента. Используйте только буквы и пробелы");
        }

        // Проверка суммы продажи (должна быть положительной)
        if (sale.getSaleAmount() <= 0) {
            throw new IllegalArgumentException("Сумма продажи должна быть положительным числом");
        }
        // Проверка существования машины
        if (!carRepository.existsById(sale.getCar().getCarId())) {
            throw new IllegalArgumentException("Машина с указанным ID не существует.");
        }

        // Проверка существования пользователя
        if (!userRepository.existsById(sale.getUser().getUserId())) {
            throw new IllegalArgumentException("Пользователь с указанным ID не существует.");
        }

        saleRepository.save(sale);
    }

    // Найти продажу по ID
    public Sale getSaleById(Long id) {
        return saleRepository.findById(id).orElse(null);
    }

    // Удалить продажу по ID
    public void deleteSale(Long id) {
        saleRepository.deleteById(id);
    }
}