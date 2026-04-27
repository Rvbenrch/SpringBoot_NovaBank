package com.novabank.service.impl;

import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.SaldoInsuficienteException;
import com.novabank.exception.ValidacionException;
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

        Cuenta cuenta = cuentaRepository.findById(cuentaId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada con id: " + cuentaId)
                );

        if (movimiento.getCantidad() == null || movimiento.getCantidad() <= 0) {
            throw new ValidacionException("La cantidad del movimiento debe ser mayor que cero.");
        }

        switch (movimiento.getTipo()) {
            case DEPOSITO -> cuenta.setSaldo(cuenta.getSaldo() + movimiento.getCantidad());
            case RETIRO -> {
                if (cuenta.getSaldo() < movimiento.getCantidad()) {
                    throw new SaldoInsuficienteException("Saldo insuficiente para realizar el retiro.");
                }
                cuenta.setSaldo(cuenta.getSaldo() - movimiento.getCantidad());
            }
            default -> throw new ValidacionException("Tipo de movimiento no válido.");
        }

        movimiento.setCuenta(cuenta);

        log.info("Registrando movimiento {} de {}€ en cuenta {}",
                movimiento.getTipo(), movimiento.getCantidad(), cuenta.getNumeroCuenta());

        cuentaRepository.save(cuenta);

        return movimientoRepository.save(movimiento);
    }

    @Override
    public List<Movimiento> listarPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    @Override
    public List<Movimiento> listarPorCuentaYFechas(Long cuentaId, LocalDate inicio, LocalDate fin) {
        return movimientoRepository.findByCuentaIdAndFechaBetween(
                cuentaId,
                inicio.atStartOfDay(),
                fin.atTime(23, 59, 59)
        );
    }
}