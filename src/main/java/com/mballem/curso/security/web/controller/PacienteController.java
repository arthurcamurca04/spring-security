package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;
import com.mballem.curso.security.web.service.PacienteService;

@Controller
@RequestMapping("pacientes")
public class PacienteController {
	
	@Autowired
	private PacienteService service;
	
	@Autowired
	private UsuarioService usuarioService;
	
	//abrir a página de dados pessoais do paciente
	@GetMapping("/dados")
	public String cadastrar(Paciente paciente, ModelMap model,
			@AuthenticationPrincipal User user) {
		
		paciente = service.buscarPorUsuarioEmail(user.getUsername());
		if(paciente.hasNotId()) {
			paciente.setUsuario(new Usuario(user.getUsername()));
		}
		
		model.addAttribute("paciente", paciente);
		return "paciente/cadastro";
	}
	
	//salvar o form de dados pessoais do paciente com verificação de senha
	@PostMapping("/salvar")
	public String salvar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
			paciente.setUsuario(usuario);
			service.salvar(paciente);
			model.addAttribute("sucesso", "Seus dados foram inseridos com sucesso!");
		}else {
			model.addAttribute("falha", "Sua senha não confere! Tente novamente");
		}
		return "paciente/cadastro";		
	}
	
	//editar o form de dados pessoais do paciente com verificação de senha
	@PostMapping("/editar")
	public String editar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
		Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
		if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
			service.editar(paciente);
			model.addAttribute("sucesso", "Seus dados foram editados com sucesso!");
		}else {
			model.addAttribute("falha", "Sua senha não confere! Tente novamente");
		}
		return "paciente/cadastro";		
	}	
}
