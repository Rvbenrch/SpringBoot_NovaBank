package com.novabank.controller;

import com.novabank.dto.ClienteDTO;
import com.novabank.dto.CuentaDTO;
import com.novabank.service.ClienteService;
import com.novabank.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final CuentaService cuentaService; // Inyectamos CuentaService para listar las cuentas del cliente

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO dto) {
        ClienteDTO creado = clienteService.crearCliente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerCliente(id));
    }


    @GetMapping("/{id}/cuentas")
    public ResponseEntity<List<CuentaDTO>> listarCuentasPorCliente(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.listarCuentasPorCliente(id));
    }
}