package com.example.demo.repository;

import com.example.demo.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    // Найти записи на складе по carId, отсортированные по количеству (по возрастанию)
    List<Inventory> findByCar_CarIdOrderByQuantityAsc(Long carId);

    // Найти записи на складе по carId, отсортированные по количеству (по возрастанию)
    @Query("SELECT i FROM Inventory i WHERE i.car.carId = :carId ORDER BY i.quantity ASC")
    List<Inventory> findByCarIdOrderByQuantityAsc(@Param("carId") Long carId);

    // Обновить supplier_id на NULL для всех записей на складе, связанных с поставщиком
    @Modifying
    @Query("UPDATE Inventory i SET i.supplier = NULL WHERE i.supplier.supplierId = :supplierId")
    void updateSupplierIdToNull(@Param("supplierId") Long supplierId);

    // Вернуть supplier_id для всех записей на складе, связанных с поставщиком
    @Modifying
    @Query("UPDATE Inventory i SET i.supplier.supplierId = :supplierId WHERE i.supplier IS NULL")
    void restoreSupplierId(@Param("supplierId") Long supplierId);

}