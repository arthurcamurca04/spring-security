package com.mballem.curso.security.web.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Paciente;
import com.mballem.curso.security.web.repository.PacienteRepositorio;

@Service
public class PacienteService {

	@Autowired
	private PacienteRepositorio repository;
	
	@Transactional(readOnly = true)
	public Paciente buscarPorUsuarioEmail(String email) {
		return repository.findByUsuarioEmail(email).orElse(new Paciente());
	}

	@Transactional(readOnly = false)
	public void salvar(Paciente paciente) {
		repository.save(paciente);		
	}

	@Transactional(readOnly = false)
	public void editar(Paciente paciente) {
		Paciente pctUpdate = repository.findById(paciente.getId()).get();
		pctUpdate.setNome(paciente.getNome());
		pctUpdate.setDtNascimento(paciente.getDtNascimento());
		
	}
}
