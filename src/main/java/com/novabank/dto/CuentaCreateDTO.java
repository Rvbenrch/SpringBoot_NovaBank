package com.novabank.dto;

import lombok.Data;

@Data
public class CuentaCreateDTO {

    private String numeroCuenta;
    private Long clienteId;
}