package com.novabank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CuentaCreateDTO {

    @NotBlank
    @Size(max = 34)
    private String numeroCuenta;

    @NotNull
    private Long clienteId;
}