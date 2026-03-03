package com.academix.repository;

import com.academix.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // 1. Buscador por Email (Vital para el Login y Recuperación)
    // Spring Boot genera la consulta automáticamente basándose en este nombre
    Optional<Usuario> findByEmail(String email);

    // 2. Buscador por Nombre de Usuario (Por si quieres login con username)
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
    
    // 3. Verificar si un email ya existe (Útil para el registro)
    Boolean existsByEmail(String email);
}