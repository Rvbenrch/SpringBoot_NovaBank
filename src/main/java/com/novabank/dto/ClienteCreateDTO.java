package com.novabank.dto;

import lombok.Data;

@Data
public class ClienteCreateDTO {

    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;
}