package com.example.demo.repository;

import com.example.demo.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    // Найти активных поставщиков
    List<Supplier> findByIsDeletedFalse();

    // Найти удалённых поставщиков
    List<Supplier> findByIsDeletedTrue();

    // Пометить поставщика как удалённого
    @Modifying
    @Query("UPDATE Supplier s SET s.isDeleted = true WHERE s.supplierId = :id")
    void softDelete(@Param("id") Long id);

    // Вернуть поставщика в статус активных
    @Modifying
    @Query("UPDATE Supplier s SET s.isDeleted = false WHERE s.supplierId = :id")
    void restore(@Param("id") Long id);
}