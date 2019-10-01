package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.UsuarioService;
import com.mballem.curso.security.web.service.MedicoService;

@Controller
@RequestMapping("u")
public class UsuarioController {

	@Autowired
	private UsuarioService service;

	@Autowired
	private MedicoService medService;

	// abrir o cadastro de usuarios (medico, admin, paciente)
	@GetMapping("/novo/cadastro/usuario")
	public String cadastrosuario(Usuario usuario) {
		return "usuario/cadastro";
	}

	// abrir lista de usuarios
	@GetMapping("/lista")
	public String listarUsuarios() {
		return "usuario/lista";
	}

	// listar usuarios na datatables
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatable(HttpServletRequest req) {
		return ResponseEntity.ok(service.buscarTodos(req));
	}

	// salvar cadastro de novos admins pelo administradores assim como também os
	// médicos
	@PostMapping("/cadastro/salvar")
	public String cadastroNovoUsuario(Usuario usuario, RedirectAttributes attr) {
		List<Perfil> perfis = usuario.getPerfis();
		if (perfis.size() > 2 || perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L)))
				|| perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))) {
			attr.addFlashAttribute("falha", "Paciente não pode ser ADMIN e/ou MÉDICO");
			attr.addFlashAttribute("usuario", usuario);
		} else {

			try {
				service.salvarUsuario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
			} catch (DataIntegrityViolationException e) {
				attr.addFlashAttribute("falha", "Cadastro não realizado, pois este usuário já está cadastro!");
			}
		}
		return "redirect:/u/novo/cadastro/usuario";
	}

	// pré-edição de credenciais de usuários
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
		return new ModelAndView("usuario/cadastro", "usuario", service.buscarPorId(id));
	}

	// pré-edição de credenciais de usuários
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastrosPessoais(@PathVariable("id") Long usuarioId,
			@PathVariable("perfis") Long[] perfisId) {

		Usuario us = service.buscarPorIdAndPerfis(usuarioId, perfisId);

		if (us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod()))
				&& !us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

			return new ModelAndView("usuario/cadastro", "usuario", us);

		} else if (us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

			Medico medico = medService.buscarPorUsuarioId(usuarioId);

			return medico.hasNotId() ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
					: new ModelAndView("medico/cadastro", "medico", medico);

		} else if (us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", 403);
			model.addObject("error", "Área Restrita");
			model.addObject("message", "Os dados de pacientes são restritos a eles!");
			return model;
		}

		return new ModelAndView("redirect:/u/lista");
	}

	@GetMapping("/editar/senha")
	public String abrirEditarSenha() {
		return "usuario/editar-senha";
	}

	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2,
			@RequestParam("senha3") String s3, @AuthenticationPrincipal User user, RedirectAttributes attr) {
		
		if (!s1.equals(s2)) {
			attr.addFlashAttribute("falha", "Senhas não conferem. Tente novamente!");
			return "redirect:/u/editar/senha";
		}
		
		Usuario u = service.buscarPorEmail(user.getUsername());
		if (!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
			attr.addFlashAttribute("falha", "Senha atual não confere. Tente novamente!");
			return "redirect:/u/editar/senha";
		}
		
		service.alterarSenha(u, s1);
		attr.addFlashAttribute("sucesso", "Senha alterada com sucesso!");
		return "redirect:/u/editar/senha";
	}
}
