package com.novabank.service;

import com.novabank.model.Cuenta;

import java.util.List;

public interface CuentaService {

    Cuenta crearCuenta(Cuenta cuenta, Long clienteId);

    Cuenta buscarPorId(Long id);

    Cuenta buscarPorNumeroCuenta(String numeroCuenta);

    List<Cuenta> listarPorCliente(Long clienteId);

    Cuenta cargarCuentaConMovimientos(Long id);
    void transferir(Long cuentaOrigenId, Long cuentaDestinoId, Double cantidad);
    boolean existeNumeroCuenta(String numeroCuenta);
}