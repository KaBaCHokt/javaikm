package com.example.demo.service;

import com.example.demo.entity.Supplier;
import com.example.demo.repository.SupplierRepository;
import com.example.demo.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    // Регулярные выражения для проверки
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{10,15}$";

    // Получить всех поставщиков
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    // Сохранить поставщика (добавить или обновить)
    public void saveSupplier(Supplier supplier) {
        // Проверка email
        if (!Pattern.matches(EMAIL_REGEX, supplier.getEmail())) {
            throw new IllegalArgumentException("Некорректный формат email");
        }

        // Проверка телефона
        if (!Pattern.matches(PHONE_REGEX, supplier.getPhone())) {
            throw new IllegalArgumentException("Некорректный формат номера телефона");
        }

        supplierRepository.save(supplier);
    }

    // Найти поставщика по ID
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id).orElse(null);
    }

    // Удалить поставщика по ID
    @Transactional
    public void deleteSupplier(Long id) {
        // Помечаем поставщика как удалённого
        supplierRepository.softDelete(id);

        // Обновляем supplier_id в таблице inventory на NULL
        inventoryRepository.updateSupplierIdToNull(id);
    }

    // Вернуть поставщика в статус активных
    @Transactional
    public void restoreSupplier(Long id) {
        // Помечаем поставщика как активного
        supplierRepository.restore(id);

        // Возвращаем supplier_id в таблице inventory
        inventoryRepository.restoreSupplierId(id);
    }

    // Получить активных поставщиков (isDeleted = false)
    public List<Supplier> getActiveSuppliers() {
        return supplierRepository.findByIsDeletedFalse();
    }

    // Получить удалённых поставщиков (isDeleted = true)
    public List<Supplier> getDeletedSuppliers() {
        return supplierRepository.findByIsDeletedTrue();
    }
}