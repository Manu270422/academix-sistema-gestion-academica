package com.academix.controller;

import com.academix.model.Usuario;
import com.academix.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // POST: Registrar un nuevo usuario
    @PostMapping
    public Usuario registrar(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    // 🔧 A1.1: Buscar usuario por ID
    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    // 🔐 B2: Endpoint de Login Real (Fase B)
    // Actualizado: Ahora usa getEmail() y getPassword()
    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
        // Cambiamos getCorreo() por getEmail() 
        // y getContraseña() por getPassword()
        return usuarioService.login(usuario.getEmail(), usuario.getPassword());
    }
}