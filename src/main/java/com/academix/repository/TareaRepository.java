package com.academix.repository;

import com.academix.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Busca tareas filtrando por el ID de la materia relacionada
    List<Tarea> findByMateriaIdMateria(Long idMateria);
}