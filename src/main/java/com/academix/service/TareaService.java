package com.academix.service;

import com.academix.model.Tarea;
import com.academix.repository.TareaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    // Método para guardar una tarea
    public Tarea guardarTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    // Método para listar tareas por materia
    public List<Tarea> listarPorMateria(Long idMateria) {
        return tareaRepository.findByMateriaIdMateria(idMateria);
    }

    // Método para cambiar el estado de una tarea
    public Optional<Tarea> cambiarEstado(Long idTarea, String nuevoEstado) {
        Optional<Tarea> tareaOpt = tareaRepository.findById(idTarea);

        if (tareaOpt.isPresent()) {
            Tarea tarea = tareaOpt.get();
            tarea.setEstado(nuevoEstado);
            tareaRepository.save(tarea);
        }

        return tareaOpt;
    }

    // 🔹 NUEVO: Lógica para eliminar una tarea
    public boolean eliminarTarea(Long idTarea) {
        if (tareaRepository.existsById(idTarea)) {
            tareaRepository.deleteById(idTarea);
            return true;
        }
        return false;
    }
}