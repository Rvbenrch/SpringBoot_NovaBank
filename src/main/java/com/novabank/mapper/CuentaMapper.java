package com.novabank.mapper;

import com.novabank.dto.CuentaCreateDTO;
import com.novabank.dto.CuentaDTO;
import com.novabank.model.Cuenta;

import java.util.List;
import java.util.stream.Collectors;

public class CuentaMapper {

    public static CuentaDTO toDTO(Cuenta cuenta) {
        CuentaDTO dto = new CuentaDTO();
        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setSaldo(cuenta.getSaldo());
        dto.setFechaCreacion(cuenta.getFechaCreacion());
        return dto;
    }

    public static Cuenta toEntity(CuentaCreateDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(dto.getNumeroCuenta());
        return cuenta;
    }

    public static List<CuentaDTO> toDTOList(List<Cuenta> cuentas) {
        return cuentas.stream()
                .map(CuentaMapper::toDTO)
                .collect(Collectors.toList());
    }
}