package com.novabank.controller;

import com.novabank.dto.CuentaCreateDTO;
import com.novabank.dto.CuentaDTO;
import com.novabank.mapper.CuentaMapper;
import com.novabank.model.Cuenta;
import com.novabank.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public CuentaDTO crearCuenta(@RequestBody CuentaCreateDTO dto) {
        Cuenta cuenta = CuentaMapper.toEntity(dto);
        Cuenta creada = cuentaService.crearCuenta(cuenta, dto.getClienteId());
        return CuentaMapper.toDTO(creada);
    }

    @GetMapping("/{id}")
    public CuentaDTO buscarPorId(@PathVariable Long id) {
        Cuenta cuenta = cuentaService.buscarPorId(id);
        return CuentaMapper.toDTO(cuenta);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<CuentaDTO> listarPorCliente(@PathVariable Long clienteId) {
        return cuentaService.listarPorCliente(clienteId)
                .stream()
                .map(CuentaMapper::toDTO)
                .toList();
    }
}