package com.novabank.mapper;

import com.novabank.dto.MovimientoDTO;
import com.novabank.model.Movimiento;

import java.util.List;
import java.util.stream.Collectors;

public class MovimientoMapper {

    public static MovimientoDTO toDTO(Movimiento movimiento) {
        MovimientoDTO dto = new MovimientoDTO();
        dto.setId(movimiento.getId());
        dto.setTipo(movimiento.getTipo());
        dto.setCantidad(movimiento.getCantidad());
        dto.setFecha(movimiento.getFecha());

        if (movimiento.getCuenta() != null) {
            dto.setNumeroCuenta(movimiento.getCuenta().getNumeroCuenta());
        }

        return dto;
    }

    public static Movimiento toEntity(MovimientoCreateDTO dto) {
        Movimiento movimiento = new Movimiento();
        movimiento.setTipo(dto.getTipo());
        movimiento.setCantidad(dto.getCantidad());
        return movimiento;
    }

    public static List<MovimientoDTO> toDTOList(List<Movimiento> movimientos) {
        return movimientos.stream()
                .map(MovimientoMapper::toDTO)
                .collect(Collectors.toList());
    }
}