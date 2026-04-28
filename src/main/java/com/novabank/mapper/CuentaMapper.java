package com.novabank.mapper;

import com.novabank.dto.CuentaCreateDTO;
import com.novabank.dto.CuentaDTO;
import com.novabank.model.Cuenta;

import java.math.BigDecimal;

public class CuentaMapper {

    public static Cuenta toEntity(CuentaCreateDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        cuenta.setSaldo(BigDecimal.ZERO); // saldo inicial
        return cuenta;
    }

    public static CuentaDTO toDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setSaldo(cuenta.getSaldo());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        return dto;
    }
}