package com.mballem.curso.security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.web.service.MedicoService;

@Controller
@RequestMapping("medicos")
public class MedicoController {
	
	@Autowired
	public MedicoService service;

	// abrir página de dados pessoais de medicos pelo Médico
	@GetMapping("/dados")
	public String cadastroMedico(Medico med, ModelMap model) {
		return "medico/cadastro";
	}

	// cadastrar um médico
	@GetMapping("/salvar")
	public String salvarMedico(Medico med, RedirectAttributes attr) {
		service.salvarMedico(med);
		attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");	
		attr.addFlashAttribute("medico", med);
		return "redirect:/medicos/dados";
	}

	// editar um médico
	@GetMapping("/editar")
	public String editarMedico(Medico med, RedirectAttributes attr) {
		service.editarMedico(med);
		attr.addFlashAttribute("sucesso", "Operação editado com sucesso!");	
		attr.addFlashAttribute("medico", med);
		return "redirect:/medicos/dados";
	}
}
