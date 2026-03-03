package com.academix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email; // Cambiado de 'correo' a 'email' para que coincida con el Controller

    @Column(nullable = false, unique = true)
    private String nombreUsuario; // Agregado para que funcione la búsqueda flexible

    @Column(nullable = false)
    private String password; // Cambiado de 'contraseña' a 'password' (Evita la 'ñ')

    // Constructor vacío (OBLIGATORIO JPA)
    public Usuario() {
    }

    // Getters y Setters actualizados para que el Controller los reconozca
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() { // Este es el getEmail() que el Controller pedía
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { // Este es el setPassword() que el Controller pedía
        this.password = password;
    }
}