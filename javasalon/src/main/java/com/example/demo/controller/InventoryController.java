package com.example.demo.controller;

import com.example.demo.entity.Inventory;
import com.example.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    // Показать склад
    @GetMapping
    public String getAllInventory(Model model) {
        model.addAttribute("inventory", inventoryService.getAllInventory());
        return "inventory/inventory";
    }

    // формиа для добавления на склад
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        return "inventory/add";
    }

    // Добавление в инвентарь
    @PostMapping("/add")
    public String addInventory(@ModelAttribute Inventory inventory, Model model) {
        try {
            inventoryService.saveInventory(inventory);
            return "redirect:/inventory";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "inventory/add";
        }
    }

    // форма редактирования
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Inventory inventory = inventoryService.getInventoryById(id);
        model.addAttribute("inventory", inventory);
        return "inventory/edit";
    }

    // обновление инфы
    @PostMapping("/edit/{id}")
    public String updateInventory(@PathVariable Long id, @ModelAttribute Inventory inventory, Model model) {
        try {
            inventory.setInventoryId(id);
            inventoryService.saveInventory(inventory);
            return "redirect:/inventory";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "inventory/edit";
        }
    }

    // Удалить из склада
    @GetMapping("/delete/{id}")
    public String deleteInventory(@PathVariable Long id) {
        inventoryService.deleteInventory(id);
        return "redirect:/inventory";
    }
}