package com.novabank.mapper;

import com.novabank.dto.CuentaDTO;
import com.novabank.model.Cuenta;

import java.math.BigDecimal;

public class CuentaMapper {
    public static CuentaDTO toDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setSaldo(cuenta.getSaldo());
        if (cuenta.getCliente() != null) {
            dto.setClienteId(cuenta.getCliente().getId());
        }
        return dto;
    }

    public static Cuenta toEntity(CuentaDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        // El cliente se busca y asigna en el Service, no aquí.
        return cuenta;
    }
}