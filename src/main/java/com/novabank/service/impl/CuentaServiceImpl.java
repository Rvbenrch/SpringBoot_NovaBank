package com.novabank.service.impl;

import com.novabank.model.Cuenta;
import com.novabank.repository.CuentaRepository;
import com.novabank.service.CuentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;

    @Override
    public Cuenta crearCuenta(Cuenta cuenta) {

        // Validación: número de cuenta único
        if (cuentaRepository.findByNumeroCuenta(cuenta.getNumeroCuenta()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una cuenta con el número: " + cuenta.getNumeroCuenta());
        }

        log.info("Creando cuenta con número {}", cuenta.getNumeroCuenta());
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Optional<Cuenta> buscarPorId(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    public Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    @Override
    public List<Cuenta> listarCuentasPorCliente(Long clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }

    @Override
    public Optional<Cuenta> cargarCuentaConMovimientos(Long id) {
        return cuentaRepository.findByIdWithMovimientos(id);
    }
}