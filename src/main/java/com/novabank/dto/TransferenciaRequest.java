package com.novabank.dto;

import lombok.Data;

@Data
public class TransferenciaRequest {
    private Long cuentaOrigenId;
    private Long cuentaDestinoId;
    private Double cantidad;
}