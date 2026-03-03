package com.academix.service;

import com.academix.model.Materia;
import com.academix.repository.MateriaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;

    public MateriaService(MateriaRepository materiaRepository) {
        this.materiaRepository = materiaRepository;
    }

    /**
     * Guarda o actualiza una materia.
     */
    public Materia guardarMateria(Materia materia) {
        return materiaRepository.save(materia);
    }

    /**
     * Obtiene la lista de materias vinculadas a un usuario específico.
     */
    public List<Materia> listarPorUsuario(Long idUsuario) {
        return materiaRepository.findByUsuarioIdUsuario(idUsuario);
    }

    /**
     * Busca una materia por su ID. 
     * Lanza una excepción si no la encuentra.
     */
    public Materia buscarPorId(Long id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Materia no encontrada con ID: " + id));
    }

    /**
     * Alias de buscarPorId para mantener compatibilidad.
     */
    public Materia obtenerMateriaPorId(Long id) {
        return buscarPorId(id);
    }

    /**
     * Elimina una materia de la base de datos por su ID.
     * CÓDIGO NUEVO: Esto soluciona el error en el MateriaController.
     */
    public void eliminarMateria(Long id) {
        // Primero verificamos si existe para evitar errores feos
        if (!materiaRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: Materia no encontrada");
        }
        materiaRepository.deleteById(id);
    }
}