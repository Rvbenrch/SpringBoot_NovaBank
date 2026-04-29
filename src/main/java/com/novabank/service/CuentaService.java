package com.novabank.service;

import com.novabank.dto.CuentaDTO;
import com.novabank.dto.MovimientoDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface CuentaService {
    CuentaDTO crearCuenta(CuentaDTO dto);
    CuentaDTO obtenerCuenta(Long id);
    List<CuentaDTO> listarCuentasPorCliente(Long clienteId);


    BigDecimal obtenerSaldo(Long cuentaId);
    List<MovimientoDTO> obtenerMovimientos(Long cuentaId);
    List<MovimientoDTO> obtenerMovimientosPorFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin);
}