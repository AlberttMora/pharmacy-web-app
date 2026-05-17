package com.farmacia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.farmacia.dao.MedicamentoRepository;
import com.farmacia.dao.PacienteRepository;
import com.farmacia.model.Medicamento;
import com.farmacia.model.Paciente;

import jakarta.transaction.Transactional;

@Service
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepo;
    private final PacienteRepository pacienteRepo;

    public MedicamentoService(MedicamentoRepository medicamentoRepo, PacienteRepository pacienteRepo) {
        this.medicamentoRepo = medicamentoRepo;
        this.pacienteRepo    = pacienteRepo;
    }

    public List<Medicamento> getAll() {
        return medicamentoRepo.findAll();
    }

    public Medicamento getById(Integer id) {
        return medicamentoRepo.findById(id).orElse(null);
    }

    public void save(Medicamento m) {
        medicamentoRepo.save(m);
    }

    @Transactional
    public void delete(Integer id) {
        Medicamento m = medicamentoRepo.findById(id).orElse(null);
        if (m == null) return;

        // Desvincula el medicamento de todos los pacientes que lo tenian
        // para que no quede un registro huerfano en paciente_medicamento
        List<Paciente> pacientes = pacienteRepo.findAll();
        for (Paciente p : pacientes) {
            if (p.getMedicamentos().removeIf(med -> med.getId().equals(id))) {
                pacienteRepo.save(p);
            }
        }

        medicamentoRepo.deleteById(id);
    }
}
