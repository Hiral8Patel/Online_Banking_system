package com.onlinebanking.controller;

import com.onlinebanking.model.User;
import com.onlinebanking.model.Account;
import com.onlinebanking.service.Phase2Phase3Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
    private final Phase2Phase3Service service;

    public MainController(Phase2Phase3Service service) { this.service = service; }

    @GetMapping({"/", "/login"})
    public String login() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model){
        User u = service.authenticate(username, password);
        if (u == null) { model.addAttribute("error", "Invalid credentials"); return "login"; }
        session.setAttribute("user", u.getUsername());
        return "redirect:/dashboard";
    }

    @GetMapping("/register")
    public String register() { return "register"; }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username, @RequestParam String password, Model model){
        boolean ok = service.register(username, password);
        if (!ok) { model.addAttribute("error", "Username already exists"); return "register"; }
        model.addAttribute("msg", "Registration successful. Please login."); return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session){
        String un = (String) session.getAttribute("user"); if (un == null) return "redirect:/login";
        User u = service.getUser(un); model.addAttribute("user", u); return "dashboard";
    }

    @PostMapping("/createAccount")
    public String createAccount(@RequestParam String accNum, @RequestParam String type, HttpSession session, Model model){
        String un = (String) session.getAttribute("user");
        boolean ok = service.createAccount(un, accNum, type);
        if (!ok) model.addAttribute("error", "Account creation failed or account exists");
        return "redirect:/dashboard";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam String accNum, @RequestParam double amount, HttpSession session, Model model){
        boolean ok = service.deposit(accNum, amount);
        if (!ok) model.addAttribute("error", "Deposit failed");
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accNum, @RequestParam double amount, HttpSession session, Model model){
        boolean ok = service.withdraw(accNum, amount);
        if (!ok) model.addAttribute("error", "Withdrawal failed: insufficient funds or invalid account");
        return "redirect:/dashboard";
    }

    @PostMapping("/transfer")
    public String transfer(@RequestParam String fromAcc, @RequestParam String toAcc, @RequestParam double amount, Model model){
        boolean ok = service.transfer(fromAcc, toAcc, amount);
        if (!ok) model.addAttribute("error", "Transfer failed: check accounts and balance");
        return "redirect:/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){ session.invalidate(); return "redirect:/login"; }
}
