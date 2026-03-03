package com.academix.controller;

import com.academix.model.Materia;

import com.academix.service.MateriaService;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/materias")

public class MateriaController {

    private final MateriaService materiaService;

    public MateriaController(MateriaService materiaService) {

        this.materiaService = materiaService;

    }

    /**

     * POST: Crear una nueva materia.

     * Recibe el objeto materia con el usuario vinculado desde el JSON.

     */

    @PostMapping

    public Materia crearMateria(@RequestBody Materia materia) {

        return materiaService.guardarMateria(materia);

    }

    /**

     * GET: Listar todas las materias de un usuario específico.

     */

    @GetMapping("/usuario/{idUsuario}")

    public List<Materia> listarMaterias(@PathVariable Long idUsuario) {

        return materiaService.listarPorUsuario(idUsuario);

    }

    /**

     * DELETE: Eliminar una materia por su ID.

     * CÓDIGO ACTUALIZADO: Maneja la eliminación en cascada de tareas 

     * (siempre que el modelo esté configurado con CascadeType.ALL).

     */

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> eliminarMateria(@PathVariable Long id) {

        try {

            materiaService.eliminarMateria(id);

            // Devolvemos 204 No Content si se eliminó con éxito

            return ResponseEntity.noContent().build();

        } catch (Exception e) {

            // Devolvemos 500 Internal Server Error si algo falla (ej. FK constraints)

            return ResponseEntity.status(500).build();

        }

    }

}