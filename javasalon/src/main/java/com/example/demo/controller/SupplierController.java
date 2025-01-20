package com.example.demo.controller;

import com.example.demo.entity.Supplier;
import com.example.demo.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    // поставщики активные/удаленные (с фильтрацией по isDeleted)
    @GetMapping
    public String getAllSuppliers(
            @RequestParam(name = "showDeleted", defaultValue = "false") boolean showDeleted,
            Model model) {
        if (showDeleted) {
            model.addAttribute("suppliers", supplierService.getDeletedSuppliers());
        } else {
            model.addAttribute("suppliers", supplierService.getActiveSuppliers());
        }
        model.addAttribute("showDeleted", showDeleted);
        return "supplier/supplier";
    }

    // форма добавления поставщика
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("supplier", new Supplier());
        return "supplier/add";
    }

    // Сохранить поставщика (добавить или обновить)
    @PostMapping("/save")
    public String saveSupplier(@ModelAttribute Supplier supplier) {
        supplierService.saveSupplier(supplier);
        return "redirect:/suppliers";
    }

    // форма редактирования поставщика
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("supplier", supplierService.getSupplierById(id));
        return "supplier/edit";
    }

    // Удалить поставщика
    @GetMapping("/delete/{id}")
    public String deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return "redirect:/suppliers";
    }

    // Вернуть поставщика в статус активных
    @GetMapping("/restore/{id}")
    public String restoreSupplier(@PathVariable Long id) {
        supplierService.restoreSupplier(id);
        return "redirect:/suppliers";
    }
}