package com.novabank.controller;

import com.novabank.dto.OperacionDTO;
import com.novabank.dto.TransferenciaDTO;
import com.novabank.service.OperacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/operaciones")
@RequiredArgsConstructor
public class OperacionController {

    private final OperacionService operacionService;

    @PostMapping("/deposito")
    public ResponseEntity<String> realizarDeposito(@Valid @RequestBody OperacionDTO dto) {
        operacionService.realizarDeposito(dto);
        return ResponseEntity.ok("Depósito realizado correctamente");
    }

    @PostMapping("/retiro")
    public ResponseEntity<String> realizarRetiro(@Valid @RequestBody OperacionDTO dto) {
        operacionService.realizarRetiro(dto);
        return ResponseEntity.ok("Retiro realizado correctamente");
    }

    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferencia(@Valid @RequestBody TransferenciaDTO dto) {
        operacionService.realizarTransferencia(dto);
        return ResponseEntity.ok("Transferencia realizada correctamente");
    }
}