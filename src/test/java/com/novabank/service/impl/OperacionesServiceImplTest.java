package com.novabank.service.impl;

import com.novabank.dto.OperacionDTO;

import com.novabank.dto.TransferenciaDTO;

import com.novabank.exception.SaldoInsuficienteException;

import com.novabank.model.Cuenta;

import com.novabank.model.Movimiento;

import com.novabank.repository.CuentaRepository;

import com.novabank.repository.MovimientoRepository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class OperacionServiceImplTest {

    @Mock

    private CuentaRepository cuentaRepository;

    @Mock

    private MovimientoRepository movimientoRepository;

    @InjectMocks

    private OperacionServiceImpl operacionService;

    @Test

    void realizarDeposito_debeAumentarSaldoYGuardarMovimiento() {

        // ARRANGE

        OperacionDTO dto = new OperacionDTO();

        dto.setNumeroCuenta("ES123");

        dto.setImporte(new BigDecimal("500.00"));

        Cuenta cuentaMock = new Cuenta();

        cuentaMock.setSaldo(new BigDecimal("1000.00"));

        when(cuentaRepository.findByNumeroCuenta("ES123")).thenReturn(Optional.of(cuentaMock));

        // ACT

        operacionService.realizarDeposito(dto);

        // ASSERT

        // Comprobamos que el saldo ha subido a 1500

        assertThat(cuentaMock.getSaldo()).isEqualTo(new BigDecimal("1500.00"));

        // Verificamos que se han guardado la cuenta y el movimiento en base de datos

        verify(cuentaRepository, times(1)).save(cuentaMock);

        verify(movimientoRepository, times(1)).save(any(Movimiento.class));

    }

    @Test

    void realizarRetiro_cuandoSaldoSuficiente_debeDisminuirSaldo() {

        // ARRANGE

        OperacionDTO dto = new OperacionDTO();

        dto.setNumeroCuenta("ES123");

        dto.setImporte(new BigDecimal("200.00"));

        Cuenta cuentaMock = new Cuenta();

        cuentaMock.setSaldo(new BigDecimal("1000.00"));

        when(cuentaRepository.findByNumeroCuenta("ES123")).thenReturn(Optional.of(cuentaMock));

        // ACT

        operacionService.realizarRetiro(dto);

        // ASSERT

        // Comprobamos que el saldo bajó a 800

        assertThat(cuentaMock.getSaldo()).isEqualTo(new BigDecimal("800.00"));

        verify(movimientoRepository, times(1)).save(any(Movimiento.class));

    }

    @Test

    void realizarRetiro_cuandoSaldoInsuficiente_debeLanzarExcepcion() {

        // ARRANGE

        OperacionDTO dto = new OperacionDTO();

        dto.setNumeroCuenta("ES123");

        dto.setImporte(new BigDecimal("5000.00")); // Intenta sacar 5000

        Cuenta cuentaMock = new Cuenta();

        cuentaMock.setSaldo(new BigDecimal("1000.00")); // Solo tiene 1000

        when(cuentaRepository.findByNumeroCuenta("ES123")).thenReturn(Optional.of(cuentaMock));

        // ACT & ASSERT

        assertThatThrownBy(() -> operacionService.realizarRetiro(dto))

                .isInstanceOf(SaldoInsuficienteException.class)

                .hasMessageContaining("Saldo insuficiente");

    }

    @Test

    void realizarTransferencia_cuandoTodoEsCorrecto_debeActualizarSaldos() {

        // ARRANGE

        TransferenciaDTO dto = new TransferenciaDTO();

        dto.setCuentaOrigen("ES111");

        dto.setCuentaDestino("ES222");

        dto.setImporte(new BigDecimal("300.00"));

        Cuenta origenMock = new Cuenta();

        origenMock.setSaldo(new BigDecimal("1000.00"));

        Cuenta destinoMock = new Cuenta();

        destinoMock.setSaldo(new BigDecimal("500.00"));

        when(cuentaRepository.findByNumeroCuenta("ES111")).thenReturn(Optional.of(origenMock));

        when(cuentaRepository.findByNumeroCuenta("ES222")).thenReturn(Optional.of(destinoMock));

        // ACT

        operacionService.realizarTransferencia(dto);

        // ASSERT

        // La de origen pierde 300, la de destino gana 300

        assertThat(origenMock.getSaldo()).isEqualTo(new BigDecimal("700.00"));

        assertThat(destinoMock.getSaldo()).isEqualTo(new BigDecimal("800.00"));

        // Se deben haber guardado 2 movimientos (uno entrante y uno saliente)

        verify(movimientoRepository, times(2)).save(any(Movimiento.class));

    }

}
