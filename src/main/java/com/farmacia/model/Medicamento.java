package com.farmacia.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    private String descripcion;

    @Column(name = "edad_recomendada", nullable = false)
    private int edadRecomendada;

    // mappedBy indica que Paciente es el dueño de la relación
    // No se usa CascadeType para que al eliminar medicamento NO se borren pacientes
    @ManyToMany(mappedBy = "medicamentos", fetch = FetchType.LAZY)
    private List<Paciente> pacientes = new ArrayList<>();

    public Medicamento() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getEdadRecomendada() { return edadRecomendada; }
    public void setEdadRecomendada(int edadRecomendada) { this.edadRecomendada = edadRecomendada; }
    public List<Paciente> getPacientes() { return pacientes; }
    public void setPacientes(List<Paciente> pacientes) { this.pacientes = pacientes; }
}
