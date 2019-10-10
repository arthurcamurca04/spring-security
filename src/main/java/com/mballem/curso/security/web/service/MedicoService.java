package com.mballem.curso.security.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.repository.MedicoRepository;

@Service
public class MedicoService {

	@Autowired
	private MedicoRepository repository;
	
	@Transactional(readOnly = true)
	public Medico buscarPorUsuarioId(Long id) {
		return repository.findByUserId(id)
				.orElse(new Medico());
	}

	@Transactional(readOnly = false)
	public void salvarMedico(Medico med) {
		repository.save(med);
		
	}

	@Transactional(readOnly = false)
	public void editarMedico(Medico med) {
		Medico m2 = repository.findById(med.getId()).get();
		m2.setCrm(med.getCrm());
		m2.setDtInscricao(med.getDtInscricao());
		m2.setNome(med.getNome());
		if (!med.getEspecialidades().isEmpty()) {
			m2.getEspecialidades().addAll(med.getEspecialidades());
		}		
	}

	@Transactional(readOnly = true)
	public Medico buscarPorEmail(String email) {
		return repository.findByUsuarioEmail(email).orElse(new Medico());
	}

	@Transactional(readOnly = false)
	public void excluirEspecialidadePorMedico(Long idMed, Long idEsp) {
		Medico medico = repository.findById(idMed).get();
		medico.getEspecialidades().removeIf(
				
				e -> e.getId().equals(idEsp)
				);		
	}

	@Transactional(readOnly = true)
	public List<Medico> buscarMedicosPorEspecialidade(String titulo) {
		return repository.findMedicosByEspecialidade(titulo);
	}

	@Transactional(readOnly = true)
	public boolean existeEspAgendada(Long idMed, Long idEsp) {
		return repository.hasEspAgendada(idMed, idEsp).isPresent();
	}
}
