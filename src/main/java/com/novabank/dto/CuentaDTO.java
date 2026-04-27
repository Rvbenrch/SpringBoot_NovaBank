package com.novabank.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CuentaDTO {

    private Long id;
    private String numeroCuenta;
    private Double saldo;
    private LocalDateTime fechaCreacion;
}