package com.novabank.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CuentaDTO {
    private Long id;
    private String numeroCuenta;
    private BigDecimal saldo;
    private Long clienteId;
}