package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;
import com.mballem.curso.security.web.service.MedicoService;

@Controller
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	public MedicoService service;
	
	@Autowired
	public UsuarioService usuarioService;	

	// abrir página de dados pessoais de medicos pelo Médico
	@GetMapping("/dados")
	public String cadastroMedico(Medico med, ModelMap model, @AuthenticationPrincipal User user) {
		if(med.hasNotId()) {
			med = service.buscarPorEmail(user.getUsername());
			model.addAttribute("medico", med);
		}
		return "medico/cadastro";
	}

	// cadastrar um médico
	@PostMapping({"/salvar"})
	public String salvarMedico(Medico med, RedirectAttributes attr,
			@AuthenticationPrincipal User user) {
		
		if(med.hasNotId() && med.getUsuario().hasNotId()) {
			Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
			med.setUsuario(usuario);
		}
		
		service.salvarMedico(med);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");	
		attr.addFlashAttribute("medico", med);
		return "redirect:/medicos/dados";
	}

	// editar um médico
	@PostMapping({"/editar"})
	public String editarMedico(Medico med, RedirectAttributes attr) {
		service.editarMedico(med);
		attr.addFlashAttribute("sucesso", "Operação editado com sucesso!");	
		attr.addFlashAttribute("medico", med);
		return "redirect:/medicos/dados";
	}
	
	//excluir especialidade
	@GetMapping({"/id/{idMed}/excluir/especializacao/{idEsp}"})
	public String excluirEspecialidade(@PathVariable("idMed") Long idMed, @PathVariable("idEsp") Long idEsp,
			RedirectAttributes attr) {
		
		if ( service.existeEspAgendada(idMed, idEsp) ) {
			attr.addFlashAttribute("falha", "Exclusão negada. Especialidade possui consultas vinculadas");	
		} else {
			service.excluirEspecialidadePorMedico(idMed, idEsp);
			attr.addFlashAttribute("sucesso", "Especialidade removida com sucesso!");	
		}
		
		return "redirect:/medicos/dados";
	}
	
	@GetMapping("/especialidade/titulo/{titulo}")
	public ResponseEntity<?> getMedicoPorEspecialidade(@PathVariable("titulo") String titulo) {
		return ResponseEntity.ok(service.buscarMedicosPorEspecialidade(titulo));
	}
}
