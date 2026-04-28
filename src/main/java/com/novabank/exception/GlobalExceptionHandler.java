package com.novabank.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse buildError(String mensaje, HttpServletRequest request) {
        return ErrorResponse.builder()
                .mensaje(mensaje)
                .timestamp(LocalDateTime.now())
                .ruta(request.getRequestURI())
                .build();
    }

    // 404 - Recurso no encontrado
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(RecursoNoEncontradoException ex, HttpServletRequest request) {
        log.warn("404 - {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(ex.getMessage(), request));
    }

    // 400 - Validación
    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidacionException ex, HttpServletRequest request) {
        log.warn("400 - {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(ex.getMessage(), request));
    }

    // 409 - Saldo insuficiente
    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ErrorResponse> handleSaldoInsuficiente(SaldoInsuficienteException ex, HttpServletRequest request) {
        log.warn("409 - {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(ex.getMessage(), request));
    }

    // 400 - Errores de validación de DTOs (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("Datos inválidos");

        log.warn("400 - {}", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(mensaje, request));
    }

    // 400 - Parámetros incorrectos en la URL
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        String mensaje = "Parámetro inválido: " + ex.getName();
        log.warn("400 - {}", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(mensaje, request));
    }

    // 500 - Errores inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("500 - Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("Error interno del servidor", request));
    }
}