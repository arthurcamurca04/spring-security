package com.mballem.curso.security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

	// abrir pagina home
	@GetMapping({"/",  "/home"})
	public String home() {
		return "home";
	}
	
	//abrir página login
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	//login inválido
	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais Inválidas");
		model.addAttribute("texto", "Login ou Senha incorretos. Tente novamente!");
		model.addAttribute("subtexto", "Acesso permitido apenas para cadastros já ativados!");
		return "login";
	}
	
	
}
