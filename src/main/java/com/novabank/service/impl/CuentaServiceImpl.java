package com.novabank.service.impl;

import com.novabank.dto.CuentaDTO;
import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.ValidacionException;
import com.novabank.mapper.CuentaMapper;
import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.repository.ClienteRepository;
import com.novabank.repository.CuentaRepository;
import com.novabank.service.CuentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    @Override
    public CuentaDTO crearCuenta(CuentaCreateDTO dto) {

        // Validar duplicado de número de cuenta
        if (cuentaRepository.findByNumeroCuenta(dto.getNumeroCuenta()).isPresent()) {
            throw new ValidacionException("Ya existe una cuenta con el número: " + dto.getNumeroCuenta());
        }

        // Validar cliente existente
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cliente no encontrado con id: " + dto.getClienteId())
                );

        // Crear entidad
        Cuenta cuenta = CuentaMapper.toEntity(dto);
        cuenta.setCliente(cliente);

        Cuenta guardada = cuentaRepository.save(cuenta);

        log.info("Cuenta {} creada para cliente {}", guardada.getNumeroCuenta(), cliente.getId());

        return CuentaMapper.toDTO(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada con id: " + id)
                );

        return CuentaMapper.toDTO(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaPorNumero(String numeroCuenta) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada con número: " + numeroCuenta)
                );

        return CuentaMapper.toDTO(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> listarCuentasPorCliente(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNoEncontradoException("Cliente no encontrado con id: " + clienteId);
        }

        return cuentaRepository.findByClienteId(clienteId)
                .stream()
                .map(CuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaDTO obtenerCuentaConMovimientos(Long cuentaId) {

        Cuenta cuenta = cuentaRepository.findByClienteIdWithMovimientos(cuentaId)
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada con id: " + cuentaId)
                );

        return CuentaMapper.toDTO(cuenta);
    }

    @Override
    public CuentaConMovimientosDTO obtenerCuentasConMovimientos(Long id) {
        return null;
    }
}