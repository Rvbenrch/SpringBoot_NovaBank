package com.novabank.service.impl;

import com.novabank.dto.OperacionDTO;
import com.novabank.dto.TransferenciaDTO;
import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.SaldoInsuficienteException;
import com.novabank.exception.ValidacionException;
import com.novabank.model.Cuenta;
import com.novabank.model.Movimiento;
import com.novabank.model.TipoMovimiento;
import com.novabank.repository.CuentaRepository;
import com.novabank.repository.MovimientoRepository;
import com.novabank.service.OperacionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OperacionServiceImpl implements OperacionService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Override
    public void realizarDeposito(OperacionDTO dto) {

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(dto.getNumeroCuenta())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada: " + dto.getNumeroCuenta())
                );

        BigDecimal importe = dto.getImporte();

        if (importe == null || importe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionException("El importe debe ser mayor que cero");
        }

        cuenta.setSaldo(cuenta.getSaldo().add(importe));

        Movimiento movimiento = Movimiento.builder()
                .cuenta(cuenta)
                .tipo(TipoMovimiento.DEPOSITO)
                .cantidad(importe)
                .build();

        cuentaRepository.save(cuenta);
        movimientoRepository.save(movimiento);

        log.info("Depósito de {} en cuenta {}", importe, dto.getNumeroCuenta());
    }

    @Override
    public void realizarRetiro(OperacionDTO dto) {

        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(dto.getNumeroCuenta())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada: " + dto.getNumeroCuenta())
                );

        BigDecimal importe = dto.getImporte();

        if (importe == null || importe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionException("El importe debe ser mayor que cero");
        }

        if (cuenta.getSaldo().compareTo(importe) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar el retiro");
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(importe));

        Movimiento movimiento = Movimiento.builder()
                .cuenta(cuenta)
                .tipo(TipoMovimiento.RETIRO)
                .cantidad(importe)
                .build();

        cuentaRepository.save(cuenta);
        movimientoRepository.save(movimiento);

        log.info("Retiro de {} en cuenta {}", importe, dto.getNumeroCuenta());
    }

    @Override
    public void realizarTransferencia(TransferenciaDTO dto) {

        BigDecimal importe = dto.getImporte();

        if (importe == null || importe.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidacionException("El importe debe ser mayor que cero");
        }

        Cuenta origen = cuentaRepository.findByNumeroCuenta(dto.getCuentaOrigen())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta origen no encontrada: " + dto.getCuentaOrigen())
                );

        Cuenta destino = cuentaRepository.findByNumeroCuenta(dto.getCuentaDestino())
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta destino no encontrada: " + dto.getCuentaDestino())
                );

        if (origen.getSaldo().compareTo(importe) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta origen");
        }

        // Actualizar saldos
        origen.setSaldo(origen.getSaldo().subtract(importe));
        destino.setSaldo(destino.getSaldo().add(importe));

        // Movimiento saliente
        Movimiento movSalida = Movimiento.builder()
                .cuenta(origen)
                .tipo(TipoMovimiento.TRANSFERENCIA_SALIENTE)
                .cantidad(importe)
                .build();

        // Movimiento entrante
        Movimiento movEntrada = Movimiento.builder()
                .cuenta(destino)
                .tipo(TipoMovimiento.TRANSFERENCIA_ENTRANTE)
                .cantidad(importe)
                .build();

        // Guardar
        cuentaRepository.save(origen);
        cuentaRepository.save(destino);
        movimientoRepository.save(movSalida);
        movimientoRepository.save(movEntrada);

        log.info("Transferencia de {} desde {} hacia {}",
                importe, dto.getCuentaOrigen(), dto.getCuentaDestino());
    }
}