package com.academix.controller;

import com.academix.model.TokenRecuperacion;
import com.academix.model.Usuario;
import com.academix.repository.TokenRepository;
import com.academix.repository.UsuarioRepository;
import com.academix.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importación necesaria
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    // 1. SOLICITAR RECUPERACIÓN (Paso 1)
    @PostMapping("/recuperar")
    public ResponseEntity<?> solicitarRecuperacion(@RequestBody Map<String, String> request) {
        String emailODato = request.get("email");

        // Buscar al usuario por email o nombre de usuario
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(emailODato);
        if (usuarioOpt.isEmpty()) {
            usuarioOpt = usuarioRepository.findByNombreUsuario(emailODato);
        }

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            // Generar un Token único
            String tokenStr = UUID.randomUUID().toString();
            
            // Guardar el token con expiración de 30 min
            TokenRecuperacion token = new TokenRecuperacion();
            token.setToken(tokenStr);
            token.setUsuario(usuario);
            token.setFechaExpiracion(LocalDateTime.now().plusMinutes(30));
            
            tokenRepository.save(token);

            // Armar el enlace
            String enlace = "http://localhost:8080/restablecer.html?token=" + tokenStr;
            
            try {
                emailService.enviarCorreoRecuperacion(usuario.getEmail(), usuario.getNombre(), enlace);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error al enviar el correo");
            }
        }

        // Siempre respondemos OK por seguridad (evitar enumeración de usuarios)
        return ResponseEntity.ok().body(Map.of("message", "Instrucciones enviadas si el usuario existe."));
    }

    // 2. ACTUALIZAR CONTRASEÑA (Paso 2 - Desde el correo)
    @PostMapping("/actualizar-password")
    public ResponseEntity<?> actualizarPassword(@RequestBody Map<String, String> request) {
        String tokenStr = request.get("token");
        String nuevaPassword = request.get("nuevaPassword");

        Optional<TokenRecuperacion> tokenOpt = tokenRepository.findByToken(tokenStr);

        // Validar que el token exista y no haya expirado
        if (tokenOpt.isPresent() && tokenOpt.get().getFechaExpiracion().isAfter(LocalDateTime.now())) {
            Usuario usuario = tokenOpt.get().getUsuario();
            
            // Cifrar la nueva contraseña con BCrypt
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            usuario.setPassword(encoder.encode(nuevaPassword));
            
            usuarioRepository.save(usuario);
            
            // Borrar el token para que no se pueda reutilizar
            tokenRepository.delete(tokenOpt.get());
            
            return ResponseEntity.ok().body(Map.of("message", "Contraseña actualizada correctamente"));
        }

        return ResponseEntity.status(400).body("El enlace es inválido o ha expirado");
    }
}