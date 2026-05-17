package com.farmacia.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.farmacia.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Integer> {}