package com.novabank.service.impl;

import com.novabank.model.Cuenta;
import com.novabank.model.Movimiento;
import com.novabank.repository.CuentaRepository;
import com.novabank.repository.MovimientoRepository;
import com.novabank.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Override
    public Movimiento registrarMovimiento(Movimiento movimiento, Long cuentaId) {

        // Validar cuenta
        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con id: " + cuentaId));

        // Validar cantidad
        if (movimiento.getCantidad() == null || movimiento.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad del movimiento debe ser mayor que cero.");
        }

        // Lógica de negocio del CP3
        switch (movimiento.getTipo()) {
            case DEPOSITO -> cuenta.setSaldo(cuenta.getSaldo() + movimiento.getCantidad());
            case RETIRO -> {
                if (cuenta.getSaldo() < movimiento.getCantidad()) {
                    throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro.");
                }
                cuenta.setSaldo(cuenta.getSaldo() - movimiento.getCantidad());
            }
            default -> throw new IllegalArgumentException("Tipo de movimiento no válido.");
        }


        movimiento.setCuenta(cuenta);

        log.info("Registrando movimiento {} de {}€ en cuenta {}",
                movimiento.getTipo(), movimiento.getCantidad(), cuenta.getNumeroCuenta());

        // Guardar primero la cuenta actualizada
        cuentaRepository.save(cuenta);

        // Guardar el movimiento
        return movimientoRepository.save(movimiento);
    }

    @Override
    public List<Movimiento> listarPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    @Override
    public List<Movimiento> listarPorCuentaYFechas(Long cuentaId, LocalDate inicio, LocalDate fin) {
        return movimientoRepository.findByCuentaIdAndFechaBetween(cuentaId, inicio.atStartOfDay(), fin.atTime(23, 59, 59));
    }
}