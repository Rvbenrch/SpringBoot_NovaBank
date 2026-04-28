package com.novabank.dto;
import com.novabank.model.TipoMovimiento;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MovimientoDTO {
    private Long id;
    private TipoMovimiento tipo;
    private Double cantidad;
    private LocalDateTime fecha;
    private Long cuentaId; // Para asociarlo en el POST
}