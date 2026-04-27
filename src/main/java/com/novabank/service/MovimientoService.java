package com.novabank.service;

import com.novabank.model.Movimiento;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {

    Movimiento registrarMovimiento(Movimiento movimiento);

    List<Movimiento> listarPorCuenta(Long cuentaId);

    List<Movimiento> listarPorCuentaYFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin);
}