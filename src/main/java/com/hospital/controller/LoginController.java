package com.hospital.controller;

import com.hospital.model.Users;
import com.hospital.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String loginHome(){return "login";}

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Users user = usersRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) { // Replace with hashed password check
            session.setAttribute("user", user);

            // Check if user is a receptionist
            if (user.getRole() == Users.Role.RECEPTIONIST) {
                return "redirect:/receptionist/dashboard";
            }
            else if (user.getRole() == Users.Role.DOCTOR) {
                return "redirect:/doctor/dashboard"; // Redirect to doctor dashboard
            }
            else if (user.getRole() == Users.Role.PATIENT) {
                return "redirect:/patient/dashboard"; // Redirect to patient dashboard
            }
        }

        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    @GetMapping("/receptionist/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        return "recdashboard"; // Create a dashboard page
    }

    @GetMapping("/doctor/dashboard")
    public String doctorDashboard(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        return "docdashboard";
    }

    @GetMapping("/patient/dashboard")
    public String patientDashboard(HttpSession session, Model model) {
        Users user = (Users) session.getAttribute("user");
        model.addAttribute("user", user);
        return "patdashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
