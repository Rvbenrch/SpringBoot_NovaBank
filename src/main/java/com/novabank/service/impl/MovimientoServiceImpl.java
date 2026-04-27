package com.novabank.service.impl;

import com.novabank.model.Movimiento;
import com.novabank.repository.MovimientoRepository;
import com.novabank.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;

    @Override
    public Movimiento registrarMovimiento(Movimiento movimiento) {

        if (movimiento.getCantidad() == null || movimiento.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad del movimiento debe ser mayor que cero.");
        }

        if (movimiento.getCuenta() == null) {
            throw new IllegalArgumentException("El movimiento debe estar asociado a una cuenta.");
        }

        log.info("Registrando movimiento de tipo {} por {}€ en la cuenta {}",
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getCuenta().getNumeroCuenta()
        );

        return movimientoRepository.save(movimiento);
    }

    @Override
    public List<Movimiento> listarPorCuenta(Long cuentaId) {
        return movimientoRepository.findByCuentaId(cuentaId);
    }

    @Override
    public List<Movimiento> listarPorCuentaYFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.findByCuentaIdAndFechaBetween(cuentaId, inicio, fin);
    }
}