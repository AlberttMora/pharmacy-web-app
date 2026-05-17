package com.farmacia.service;

import com.farmacia.dao.PacienteRepository;
import com.farmacia.model.Paciente;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository repo;

    public PacienteService(PacienteRepository repo) {
        this.repo = repo;
    }

    // @Transactional garantiza que la sesion de Hibernate este abierta
    // mientras Thymeleaf accede a los medicamentos (lazy loading)
    @Transactional
    public List<Paciente> getAll() {
        List<Paciente> pacientes = repo.findAll();
        // Forzar inicializacion de la lista de medicamentos de cada paciente
        pacientes.forEach(p -> p.getMedicamentos().size());
        return pacientes;
    }

    @Transactional
    public Paciente getById(Integer id) {
        Paciente p = repo.findById(id).orElse(null);
        if (p != null) p.getMedicamentos().size();
        return p;
    }

    @Transactional
    public void save(Paciente p) {
        repo.save(p);
    }

    @Transactional
    public void delete(Integer id) {
        Paciente p = repo.findById(id).orElse(null);
        if (p == null) return;
        p.getMedicamentos().clear();
        repo.save(p);
        repo.deleteById(id);
    }
}
