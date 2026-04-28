package com.novabank.service;

import com.novabank.dto.CuentaDTO;

import java.util.List;

public interface CuentaService {

    CuentaDTO crearCuenta(CuentaCreateDTO dto);

    CuentaDTO obtenerCuenta(Long id);

    CuentaDTO obtenerCuentaPorNumero(String numeroCuenta);

    List<CuentaDTO> listarCuentasPorCliente(Long clienteId);

    CuentaDTO obtenerCuentaConMovimientos(Long cuentaId);
    CuentaConMovimientosDTO obtenerCuentasConMovimientos(Long id);
}