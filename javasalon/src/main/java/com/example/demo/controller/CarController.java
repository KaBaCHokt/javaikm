package com.example.demo.controller;

import com.example.demo.entity.Car;
import com.example.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cars") // путь
public class CarController {
    @Autowired
    private CarService carService;

    // Показать все авто
    @GetMapping
    public String getAllCars(Model model) {
        model.addAttribute("cars", carService.getAllCars());
        return "car/car";
    }

    // форма для добавления авто
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("car", new Car());
        return "car/add";
    }

    // добавление авто
    @PostMapping("/add")
    public String addCar(@ModelAttribute Car car, Model model) {
        try {
            carService.saveCar(car);
            return "redirect:/cars";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "car/add";
        }
    }

    // форма для рекактирования авто
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Car car = carService.getCarById(id);
        model.addAttribute("car", car);
        return "car/edit";
    }

    // обновление инфы
    @PostMapping("/edit/{id}")
    public String updateCar(@PathVariable Long id, @ModelAttribute Car car, Model model) {
        try {
            car.setCarId(id);
            carService.saveCar(car);
            return "redirect:/cars";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "car/edit";
        }
    }

    // Удалить авто
    @GetMapping("/delete/{id}")
    public String deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return "redirect:/cars";
    }
}