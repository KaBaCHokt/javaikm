package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class UserController {

    @Autowired
    private UserService userService;

    // Показать всех клиентов
    @GetMapping
    public String getAllClients(Model model) {
        model.addAttribute("clients", userService.getAllUsers());
        return "clients/clients";
    }

    //форма добавления клиента
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("client", new User());
        return "clients/add";
    }

    // Добавить клиента
    @PostMapping("/add")
    public String addClient(@ModelAttribute("client") User client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "clients/add";
        }
        try {
            userService.saveUser(client);
            return "redirect:/clients";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // ошибка
            return "clients/add";
        }
    }

    //форма редактирования клиента
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User client = userService.getUserById(id);
        if (client == null) {
            return "redirect:/clients"; // Если клиента нет
        }
        model.addAttribute("client", client); // Добавляем найденного клиента в модель
        return "clients/edit";
    }

    // Обновить клиента
    @PostMapping("/edit/{id}")
    public String updateClient(@PathVariable Long id, @ModelAttribute("client") User client, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "clients/edit"; // при ошибке
        }
        try {
            client.setUserId(id); //  ID клиента
            userService.saveUser(client);
            return "redirect:/clients";
        } catch (ObjectOptimisticLockingFailureException e) {
            model.addAttribute("error", "Данные были изменены другим пользователем. Пожалуйста, обновите страницу и попробуйте снова.");
            return "clients/edit";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "clients/edit";
        }
    }

    // Удалить клиента
    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/clients";
    }
}