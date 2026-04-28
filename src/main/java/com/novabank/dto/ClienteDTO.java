package com.novabank.dto;
import lombok.Data;
import java.util.List;

@Data
public class ClienteDTO {
    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;
    private List<CuentaDTO> cuentas; // Para ver sus cuentas al consultar el cliente
}