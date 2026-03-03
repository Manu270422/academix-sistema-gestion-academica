package com.academix.controller;

import com.academix.model.Materia;
import com.academix.model.Tarea;
import com.academix.service.MateriaService;
import com.academix.service.TareaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private final TareaService tareaService;
    private final MateriaService materiaService;

    public TareaController(TareaService tareaService, MateriaService materiaService) {
        this.tareaService = tareaService;
        this.materiaService = materiaService;
    }

    /**
     * POST: Crear una tarea.
     * CÓDIGO BLINDADO: Se asegura de que la tarea siempre tenga un estado
     * inicial y se vincula automáticamente mediante el JSON recibido.
     */
    @PostMapping
    public Tarea crearTarea(@RequestBody Tarea tarea) {
        if (tarea.getEstado() == null || tarea.getEstado().isEmpty()) {
            tarea.setEstado("PENDIENTE");
        }
        return tareaService.guardarTarea(tarea);
    }

    /**
     * PUT: Cambiar el estado de la tarea a COMPLETADA.
     * Ideal para el flujo de trabajo del frontend.
     */
    @PutMapping("/{id}/completar")
    public ResponseEntity<Tarea> completarTarea(@PathVariable Long id) {
        // Usamos cambiarEstado del service o buscamos y actualizamos
        return tareaService.cambiarEstado(id, "COMPLETADA")
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * GET: Listar todas las tareas de una materia específica.
     */
    @GetMapping("/materia/{idMateria}")
    public List<Tarea> listarTareas(@PathVariable Long idMateria) {
        return tareaService.listarPorMateria(idMateria);
    }

    /**
     * PATCH: Cambiar el estado de una tarea a cualquier valor (Ej: ENTREGADA).
     */
    @PatchMapping("/{idTarea}/estado")
    public Tarea cambiarEstado(@PathVariable Long idTarea, @RequestParam String estado) {
        return tareaService
                .cambiarEstado(idTarea, estado.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + idTarea));
    }

    /**
     * DELETE: Eliminar una tarea por su ID.
     */
    @DeleteMapping("/{idTarea}")
    public void eliminarTarea(@PathVariable Long idTarea) {
        boolean eliminado = tareaService.eliminarTarea(idTarea);
        if (!eliminado) {
            throw new RuntimeException("No se pudo eliminar: Tarea no encontrada con ID: " + idTarea);
        }
    }
}