package com.academix.repository;

import com.academix.model.TokenRecuperacion;
import com.academix.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<TokenRecuperacion, Long> {

    // Buscar un token específico en la base de datos
    Optional<TokenRecuperacion> findByToken(String token);

    // Buscar si un usuario ya tiene un token generado
    Optional<TokenRecuperacion> findByUsuario(Usuario usuario);
    
    // Método para borrar tokens viejos (opcional para limpieza)
    void deleteByToken(String token);
}