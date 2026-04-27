package com.novabank.controller;

import com.novabank.dto.MovimientoCreateDTO;
import com.novabank.dto.MovimientoDTO;
import com.novabank.mapper.MovimientoMapper;
import com.novabank.model.Movimiento;
import com.novabank.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public MovimientoDTO registrarMovimiento(@RequestBody MovimientoCreateDTO dto) {
        Movimiento movimiento = MovimientoMapper.toEntity(dto);
        Movimiento registrado = movimientoService.registrarMovimiento(movimiento, dto.getCuentaId());
        return MovimientoMapper.toDTO(registrado);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public List<MovimientoDTO> listarPorCuenta(@PathVariable Long cuentaId) {
        return movimientoService.listarPorCuenta(cuentaId)
                .stream()
                .map(MovimientoMapper::toDTO)
                .toList();
    }

    @GetMapping("/cuenta/{cuentaId}/rango")
    public List<MovimientoDTO> listarPorCuentaYFechas(
            @PathVariable Long cuentaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        return movimientoService.listarPorCuentaYFechas(cuentaId, inicio, fin)
                .stream()
                .map(MovimientoMapper::toDTO)
                .toList();
    }
}