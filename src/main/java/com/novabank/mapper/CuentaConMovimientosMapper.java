package com.novabank.mapper;

import com.novabank.dto.MovimientoDTO;
import com.novabank.model.Cuenta;
import com.novabank.model.Movimiento;

import java.util.stream.Collectors;

public class CuentaConMovimientosMapper {

    public static CuentaConMovimientosDTO toDTO(Cuenta cuenta) {
        CuentaConMovimientosDTO dto = new CuentaConMovimientosDTO();

        dto.setId(cuenta.getId());
        dto.setNumeroCuenta(cuenta.getNumeroCuenta());
        dto.setSaldo(cuenta.getSaldo());
        dto.setFechaCreacion(cuenta.getFechaCreacion());

        dto.setMovimientos(
                cuenta.getMovimientos()
                        .stream()
                        .map(CuentaConMovimientosMapper::mapMovimiento)
                        .collect(Collectors.toList())
        );

        return dto;
    }

    private static MovimientoDTO mapMovimiento(Movimiento mov) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(mov.getId());
        dto.setTipo(mov.getTipo());
        dto.setCantidad(mov.getCantidad());
        dto.setFecha(mov.getFecha());
        dto.setNumeroCuenta(mov.getCuenta().getNumeroCuenta());
        return dto;
    }
}