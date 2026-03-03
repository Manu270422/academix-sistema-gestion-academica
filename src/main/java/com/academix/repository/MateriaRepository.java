package com.academix.repository;

import com.academix.model.Materia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MateriaRepository extends JpaRepository<Materia, Long> {

    List<Materia> findByUsuarioIdUsuario(Long idUsuario);
}
