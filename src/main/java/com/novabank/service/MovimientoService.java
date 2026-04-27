package com.novabank.service;

import com.novabank.model.Movimiento;

import java.time.LocalDate;
import java.util.List;

public interface MovimientoService {

    Movimiento registrarMovimiento(Movimiento movimiento, Long cuentaId);

    List<Movimiento> listarPorCuenta(Long cuentaId);

    List<Movimiento> listarPorCuentaYFechas(Long cuentaId, LocalDate inicio, LocalDate fin);
}