package com.novabank.dto;
import lombok.Data;
import java.util.List;

@Data
public class CuentaDTO {
    private Long id;
    private String numeroCuenta;
    private Double saldo;
    private Long clienteId; // Crucial para el POST desde Postman
    private List<MovimientoDTO> movimientos; // Se llena solo si consultamos detalle
}