package com.novabank.dto;

import com.novabank.model.TipoMovimiento;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MovimientoDTO {
    private Long id;
    private TipoMovimiento tipo;
    private BigDecimal cantidad;
    private LocalDateTime fecha;
    private String numeroCuenta;
}