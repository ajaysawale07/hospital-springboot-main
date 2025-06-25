package com.hospital.controller;

import com.hospital.model.Users;
import com.hospital.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<Users> usersList = usersService.getAllUsers();
        model.addAttribute("users", usersList);
        return "users";
    }
}
