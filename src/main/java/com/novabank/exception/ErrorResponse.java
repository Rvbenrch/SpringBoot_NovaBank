package com.novabank.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String mensaje;
    private LocalDateTime timestamp;
    private String ruta;
}