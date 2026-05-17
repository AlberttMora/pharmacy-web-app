package com.farmacia.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.farmacia.model.Medicamento;
import com.farmacia.model.Paciente;
import com.farmacia.service.MedicamentoService;
import com.farmacia.service.PacienteService;

@Controller
public class WebController {

    private static final List<Integer> EDADES_PERMITIDAS = List.of(3, 7, 12, 16, 18);

    private final PacienteService pacienteService;
    private final MedicamentoService medicamentoService;
    private final SimpMessagingTemplate ws;

    public WebController(PacienteService pacienteService,
            MedicamentoService medicamentoService,
            SimpMessagingTemplate ws) {
        this.pacienteService = pacienteService;
        this.medicamentoService = medicamentoService;
        this.ws = ws;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pacientes", pacienteService.getAll());
        model.addAttribute("medicamentos", medicamentoService.getAll());
        model.addAttribute("edades", EDADES_PERMITIDAS);
        if (!model.containsAttribute("pacienteEdit")) {
            model.addAttribute("pacienteEdit", null);
        }
        if (!model.containsAttribute("medicamentoEdit")) {
            model.addAttribute("medicamentoEdit", null);
        }
        return "index";
    }

    @PostMapping("/guardarPaciente")
    public String guardarPaciente(Paciente p,
            @RequestParam(value = "medicamentosIds", required = false) List<Integer> ids,
            RedirectAttributes ra) {
        p.setMedicamentos(resolverMedicamentos(ids));
        pacienteService.save(p);
        ws.convertAndSend("/topic/pacientes", "nuevo");
        ra.addFlashAttribute("exito", "Paciente guardado correctamente");
        return "redirect:/";
    }

    @GetMapping("/editarPaciente/{id}")
    public String editarPaciente(@PathVariable Integer id, RedirectAttributes ra) {
        Paciente p = pacienteService.getById(id);
        if (p == null) {
            ra.addFlashAttribute("error", "Paciente no encontrado");
            return "redirect:/";
        }
        ra.addFlashAttribute("pacienteEdit", p);
        return "redirect:/";
    }

    @PostMapping("/actualizarPaciente")
    public String actualizarPaciente(Paciente p,
            @RequestParam(value = "medicamentosIds", required = false) List<Integer> ids,
            RedirectAttributes ra) {
        p.setMedicamentos(resolverMedicamentos(ids));
        pacienteService.save(p);
        ws.convertAndSend("/topic/pacientes", "actualizado");
        ra.addFlashAttribute("exito", "Paciente actualizado correctamente");
        return "redirect:/";
    }

    @GetMapping("/eliminarPaciente/{id}")
    public String eliminarPaciente(@PathVariable Integer id, RedirectAttributes ra) {
        pacienteService.delete(id);
        ws.convertAndSend("/topic/pacientes", "eliminado");
        ra.addFlashAttribute("exito", "Paciente eliminado correctamente");
        return "redirect:/";
    }

    @PostMapping("/guardarMedicamento")
    public String guardarMedicamento(Medicamento m, RedirectAttributes ra) {
        if (!EDADES_PERMITIDAS.contains(m.getEdadRecomendada())) {
            ra.addFlashAttribute("error", "Edad no permitida. Solo: 3, 7, 12, 16, 18");
            return "redirect:/";
        }
        medicamentoService.save(m);
        ws.convertAndSend("/topic/medicamentos", "nuevo");
        ra.addFlashAttribute("exito", "Medicamento guardado correctamente");
        return "redirect:/";
    }

    @GetMapping("/editarMedicamento/{id}")
    public String editarMedicamento(@PathVariable Integer id, RedirectAttributes ra) {
        Medicamento m = medicamentoService.getById(id);
        if (m == null) {
            ra.addFlashAttribute("error", "Medicamento no encontrado");
            return "redirect:/";
        }
        ra.addFlashAttribute("medicamentoEdit", m);
        return "redirect:/";
    }

    @PostMapping("/actualizarMedicamento")
    public String actualizarMedicamento(Medicamento m, RedirectAttributes ra) {
        if (!EDADES_PERMITIDAS.contains(m.getEdadRecomendada())) {
            ra.addFlashAttribute("error", "Edad no permitida. Solo: 3, 7, 12, 16, 18");
            return "redirect:/";
        }
        medicamentoService.save(m);
        ws.convertAndSend("/topic/medicamentos", "actualizado");
        ra.addFlashAttribute("exito", "Medicamento actualizado correctamente");
        return "redirect:/";
    }

    @GetMapping("/eliminarMedicamento/{id}")
    public String eliminarMedicamento(@PathVariable Integer id, RedirectAttributes ra) {
        medicamentoService.delete(id);
        ws.convertAndSend("/topic/medicamentos", "eliminado");
        ra.addFlashAttribute("exito", "Medicamento eliminado. Los pacientes asociados conservan su historial");
        return "redirect:/";
    }

    private List<Medicamento> resolverMedicamentos(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }
        return medicamentoService.getAll().stream()
                .filter(m -> ids.contains(m.getId()))
                .toList();
    }
}
