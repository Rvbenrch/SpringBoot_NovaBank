package com.novabank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CuentaDTO {

    private Long id;
    private String numeroCuenta;
    private BigDecimal saldo;
    private LocalDateTime fechaCreacion;
}