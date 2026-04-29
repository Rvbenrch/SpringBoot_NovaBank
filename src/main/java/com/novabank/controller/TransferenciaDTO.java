package com.novabank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferenciaDTO {

    @NotBlank(message = "La cuenta de origen es obligatoria")
    private String cuentaOrigen;

    @NotBlank(message = "La cuenta de destino es obligatoria")
    private String cuentaDestino;

    @NotNull(message = "El importe es obligatorio")
    @DecimalMin(value = "0.01", message = "El importe mínimo debe ser 0.01")
    private BigDecimal importe;

    private String concepto;
}