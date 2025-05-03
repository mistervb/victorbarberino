package br.com.victorbarberino.portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, 
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        
        if (error != null) {
            model.addAttribute("errorMsg", "Credenciais inválidas. Por favor, tente novamente.");
        }

        if (logout != null) {
            model.addAttribute("logoutMsg", "Você foi desconectado com sucesso.");
        }
        
        return "auth/login";
    }
}
