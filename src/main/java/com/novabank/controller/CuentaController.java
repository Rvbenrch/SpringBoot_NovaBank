package com.novabank.controller;

import com.novabank.dto.CuentaDTO;
import com.novabank.dto.TransferenciaDTO;
import com.novabank.dto.OperacionDTO;
import com.novabank.service.CuentaService;
import com.novabank.service.OperacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;
    private final OperacionService operacionService;

    // Crear cuenta
    @PostMapping
    public ResponseEntity<CuentaDTO> crearCuenta(@Valid @RequestBody CuentaCreateDTO dto) {
        CuentaDTO creada = cuentaService.crearCuenta(dto);
        return ResponseEntity.ok(creada);
    }

    // Obtener cuenta por ID
    @GetMapping("/{id}")
    public ResponseEntity<CuentaDTO> obtenerCuenta(@PathVariable Long id) {
        CuentaDTO cuenta = cuentaService.obtenerCuenta(id);
        return ResponseEntity.ok(cuenta);
    }

    // Listar cuentas por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<CuentaDTO>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(cuentaService.listarCuentasPorCliente(clienteId));
    }

    // Obtener cuenta con movimientos
    @GetMapping("/{id}/movimientos")
    public ResponseEntity<CuentaDTO> obtenerCuentaConMovimientos(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerCuentaConMovimientos(id));
    }

    // Depósito
    @PostMapping("/deposito")
    public ResponseEntity<String> realizarDeposito(@Valid @RequestBody OperacionDTO dto) {
        operacionService.realizarDeposito(dto);
        return ResponseEntity.ok("Depósito realizado correctamente");
    }

    // Retiro
    @PostMapping("/retiro")
    public ResponseEntity<String> realizarRetiro(@Valid @RequestBody OperacionDTO dto) {
        operacionService.realizarRetiro(dto);
        return ResponseEntity.ok("Retiro realizado correctamente");
    }

    // Transferencia
    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferencia(@Valid @RequestBody TransferenciaDTO dto) {
        operacionService.realizarTransferencia(dto);
        return ResponseEntity.ok("Transferencia realizada correctamente");
    }
}