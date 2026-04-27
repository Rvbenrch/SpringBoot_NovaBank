package com.novabank.service.impl;

import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.repository.ClienteRepository;
import com.novabank.repository.CuentaRepository;
import com.novabank.service.CuentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public Cuenta crearCuenta(Cuenta cuenta, Long clienteId) {

        // Validación: cliente debe existir
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + clienteId));

        if (cuentaRepository.findByNumeroCuenta(cuenta.getNumeroCuenta()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una cuenta con el número: " + cuenta.getNumeroCuenta());
        }

        // Asignar cliente
        cuenta.setCliente(cliente);

        // Inicializar saldo si viene null
        if (cuenta.getSaldo() == null) {
            cuenta.setSaldo(0.0);
        }

        log.info("Creando cuenta {} para cliente {}", cuenta.getNumeroCuenta(), clienteId);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta buscarPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + id));
    }

    @Override
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con número: " + numeroCuenta));
    }

    @Override
    public List<Cuenta> listarPorCliente(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente no encontrado con id: " + clienteId);
        }

        return cuentaRepository.findByClienteId(clienteId);
    }

    @Override
    public Cuenta cargarCuentaConMovimientos(Long id) {
        return cuentaRepository.findByIdWithMovimientos(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + id));
    }

    @Override
    public boolean existeNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta).isPresent();
    }
}