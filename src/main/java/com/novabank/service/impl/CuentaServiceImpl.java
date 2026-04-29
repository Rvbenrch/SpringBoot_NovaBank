package com.novabank.service.impl;

import com.novabank.dto.CuentaDTO;
import com.novabank.dto.MovimientoDTO;
import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.ValidacionException;
import com.novabank.mapper.CuentaMapper;
import com.novabank.mapper.MovimientoMapper;
import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.repository.ClienteRepository;
import com.novabank.repository.CuentaRepository;
import com.novabank.repository.MovimientoRepository;
import com.novabank.service.CuentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimientoRepository movimientoRepository; // Añadido para consultar movimientos

    @Override
    public CuentaDTO crearCuenta(CuentaDTO dto) {
        if (cuentaRepository.findByNumeroCuenta(dto.getNumeroCuenta()).isPresent()) {
            throw new ValidacionException("Ya existe una cuenta con el número: " + dto.getNumeroCuenta());
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id: " + dto.getClienteId()));

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
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada con id: " + id));
        return CuentaMapper.toDTO(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CuentaDTO> listarCuentasPorCliente(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNoEncontradoException("Cliente no encontrado con id: " + clienteId);
        }
        return cuentaRepository.findByClienteId(clienteId).stream()
                .map(CuentaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldo(Long cuentaId) {
        return obtenerCuenta(cuentaId).getSaldo();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerMovimientos(Long cuentaId) {
        if (!cuentaRepository.existsById(cuentaId)) {
            throw new RecursoNoEncontradoException("Cuenta no encontrada con id: " + cuentaId);
        }
        return MovimientoMapper.toDTOList(movimientoRepository.findByCuentaIdOrderByFechaDesc(cuentaId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovimientoDTO> obtenerMovimientosPorFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin) {
        if (!cuentaRepository.existsById(cuentaId)) {
            throw new RecursoNoEncontradoException("Cuenta no encontrada con id: " + cuentaId);
        }
        return MovimientoMapper.toDTOList(movimientoRepository.findByCuentaIdAndFechaBetweenOrderByFechaDesc(cuentaId, inicio, fin));
    }
}