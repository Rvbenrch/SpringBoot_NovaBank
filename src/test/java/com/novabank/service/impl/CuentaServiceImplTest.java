package com.novabank.service.impl;

import com.novabank.dto.CuentaDTO;

import com.novabank.exception.RecursoNoEncontradoException;

import com.novabank.exception.ValidacionException;

import com.novabank.model.Cliente;

import com.novabank.model.Cuenta;

import com.novabank.repository.ClienteRepository;

import com.novabank.repository.CuentaRepository;

import com.novabank.repository.MovimientoRepository;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;

import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

class CuentaServiceImplTest {

    @Mock

    private CuentaRepository cuentaRepository;

    @Mock

    private ClienteRepository clienteRepository;

    @Mock

    private MovimientoRepository movimientoRepository;

    @InjectMocks

    private CuentaServiceImpl cuentaService;

    @Test

    void crearCuenta_cuandoTodoEsCorrecto_debeRetornarCuenta() {

        // ARRANGE

        CuentaDTO dto = new CuentaDTO();

        dto.setNumeroCuenta("ES12345");

        dto.setClienteId(1L);

        Cliente clienteMocK = new Cliente();

        clienteMocK.setId(1L);

        Cuenta cuentaGuardada = new Cuenta(1L, "ES12345", BigDecimal.ZERO, LocalDateTime.now(), clienteMocK, null);

        when(cuentaRepository.findByNumeroCuenta("ES12345")).thenReturn(Optional.empty());

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteMocK));

        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaGuardada);

        // ACT

        CuentaDTO resultado = cuentaService.crearCuenta(dto);

        // ASSERT

        assertThat(resultado).isNotNull();

        assertThat(resultado.getNumeroCuenta()).isEqualTo("ES12345");

        assertThat(resultado.getSaldo()).isEqualTo(BigDecimal.ZERO);

    }

    @Test

    void crearCuenta_cuandoNumeroYaExiste_debeLanzarExcepcion() {

        CuentaDTO dto = new CuentaDTO();

        dto.setNumeroCuenta("ES12345");

        when(cuentaRepository.findByNumeroCuenta("ES12345")).thenReturn(Optional.of(new Cuenta()));

        assertThatThrownBy(() -> cuentaService.crearCuenta(dto))

                .isInstanceOf(ValidacionException.class)

                .hasMessageContaining("Ya existe una cuenta con el número");

    }

    @Test

    void obtenerSaldo_debeRetornarSaldoCorrecto() {

        Cuenta cuentaMock = new Cuenta();

        cuentaMock.setSaldo(new BigDecimal("1500.50"));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaMock));

        BigDecimal saldo = cuentaService.obtenerSaldo(1L);

        assertThat(saldo).isEqualTo(new BigDecimal("1500.50"));

    }

}
