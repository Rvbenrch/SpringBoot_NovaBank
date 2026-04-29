package com.novabank.controller;

import com.novabank.dto.CuentaDTO;
import com.novabank.dto.MovimientoDTO;
import com.novabank.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaDTO> crearCuenta(@Valid @RequestBody CuentaDTO dto) {
        CuentaDTO creada = cuentaService.crearCuenta(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> obtenerCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerCuenta(id));
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> obtenerSaldo(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerSaldo(id));
    }

    // Maneja tanto la petición normal como la de rango de fechas
    @GetMapping("/{id}/movimientos")
    public ResponseEntity<List<MovimientoDTO>> listarMovimientos(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        if (fechaInicio != null && fechaFin != null) {
            return ResponseEntity.ok(cuentaService.obtenerMovimientosPorFechas(id, fechaInicio, fechaFin));
        }
        return ResponseEntity.ok(cuentaService.obtenerMovimientos(id));
    }
}