package com.novabank.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClienteDTO {

    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;
    private LocalDateTime fechaCreacion;
    private int numeroCuentas;
}