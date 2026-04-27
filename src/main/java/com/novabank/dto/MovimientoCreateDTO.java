package com.novabank.dto;

import com.novabank.model.TipoMovimiento;
import lombok.Data;

@Data
public class MovimientoCreateDTO {

    private TipoMovimiento tipo;
    private Double cantidad;
    private Long cuentaId;
}