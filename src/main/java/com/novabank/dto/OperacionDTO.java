package com.novabank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OperacionDTO {

    @NotBlank
    private String numeroCuenta;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal importe;

    private String descripcion;
}