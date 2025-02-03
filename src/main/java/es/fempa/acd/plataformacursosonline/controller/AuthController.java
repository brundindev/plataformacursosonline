package es.fempa.acd.plataformacursosonline.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.fempa.acd.plataformacursosonline.service.CustomUserDetailsService.CustomUserDetails;

import java.security.Principal;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Principal principal) {
        CustomUserDetails userDetails = (CustomUserDetails) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        return "admin/dashboard";
    }
    
    
}
