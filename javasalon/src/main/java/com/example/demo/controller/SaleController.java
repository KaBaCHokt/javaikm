package com.example.demo.controller;

import com.example.demo.entity.Sale;
import com.example.demo.entity.User;
import com.example.demo.entity.Car;
import com.example.demo.service.SaleService;
import com.example.demo.service.UserService;
import com.example.demo.service.CarService;
import com.example.demo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
//путь
@RequestMapping("/sales")
public class SaleController {
    @Autowired
    private SaleService saleService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private InventoryService inventoryService;

    // Показать продажи
    @GetMapping
    public String getAllSales(Model model) {
        model.addAttribute("sales", saleService.getAllSales());
        return "sale/sale";
    }

    // форма добавления продажи
    @GetMapping("/add")
    public String showAddForm(Model model) {
        Sale sale = new Sale();
        sale.setUser(new User()); // Инициализация user
        sale.setCar(new Car());   // Инициализация car
        model.addAttribute("sale", sale);
        return "sale/add";
    }

    // Добавить продажу
    @PostMapping("/add")
    public String addSale(@ModelAttribute Sale sale,
                          @RequestParam Long userId,
                          @RequestParam Long carId,
                          Model model) {
        try {
            // юзеры и авто по их id
            User user = userService.getUserById(userId);
            Car car = carService.getCarById(carId);

            // проверка на их существование
            if (user == null || car == null) {
                model.addAttribute("error", "User or Car not found");
                return "sale/add"; // Возврат на форму с ошибкой
            }

            // проверка на наличие авто на складе + вычет со склада
            if (!inventoryService.processCarSale(carId)) {
                model.addAttribute("error", "Car not available in inventory");
                return "sale/add"; // Возврат на форму с ошибкой
            }

            // Устанавливаем юзера и авто в Sale
            sale.setUser(user);
            sale.setCar(car);

            // Сохраняем продажу
            saleService.saveSale(sale);

            return "redirect:/sales";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "sale/add"; // Возврат на форму с ошибкой
        }
    }

    // форма изменения продажи
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Sale sale = saleService.getSaleById(id);
        model.addAttribute("sale", sale);
        return "sale/edit";
    }

    // Обновить продажу
    @PostMapping("/edit/{id}")
    public String updateSale(@PathVariable Long id,
                             @ModelAttribute Sale sale,
                             @RequestParam Long userId,
                             @RequestParam Long carId,
                             Model model) {
        try {
            // юзеры и авто по их id
            User user = userService.getUserById(userId);
            Car car = carService.getCarById(carId);

            // проверка на их существование
            if (user == null || car == null) {
                model.addAttribute("error", "User or Car not found");
                return "sale/edit"; // Возврат на форму с ошибкой
            }

            // Устанавливаем юзера и авто в Sale
            sale.setUser(user);
            sale.setCar(car);

            // ID продажи
            sale.setSaleId(id);

            // Сохраняем обновленную продажу
            saleService.saveSale(sale);
            return "redirect:/sales";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "sale/edit";
        }
    }

    // Удалить продажу
    @GetMapping("/delete/{id}")
    public String deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return "redirect:/sales";
    }
}