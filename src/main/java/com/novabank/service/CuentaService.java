package com.novabank.service;

import com.novabank.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaService {

    Cuenta crearCuenta(Cuenta cuenta);

    Optional<Cuenta> buscarPorId(Long id);

    Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta);

    List<Cuenta> listarCuentasPorCliente(Long clienteId);

    Optional<Cuenta> cargarCuentaConMovimientos(Long id);
}