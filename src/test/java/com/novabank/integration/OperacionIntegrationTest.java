package com.novabank.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.novabank.dto.OperacionDTO;

import com.novabank.model.Cliente;

import com.novabank.model.Cuenta;

import com.novabank.repository.ClienteRepository;

import com.novabank.repository.CuentaRepository;

import com.novabank.repository.MovimientoRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest

@AutoConfigureMockMvc

@ActiveProfiles("test")

@Transactional

class OperacionIntegrationTest {

    @Autowired

    private MockMvc mockMvc;

    @Autowired

    private ObjectMapper objectMapper;

    @Autowired

    private ClienteRepository clienteRepository;

    @Autowired

    private CuentaRepository cuentaRepository;

    @Autowired

    private MovimientoRepository movimientoRepository;

    @Test

    @WithMockUser

    void flujoCompleto_realizarDeposito_actualizaBDReal() throws Exception {

        // 1. LIMPIEZA PREVIA (De abajo a arriba)

        movimientoRepository.deleteAll();

        cuentaRepository.deleteAll();

        clienteRepository.deleteAll();

        // 2. ARRANGE: Preparamos la BD con un Cliente y una Cuenta a saldo 0

        Cliente cliente = new Cliente(null, "Maria", "Gomez", "99999999Z", "maria@mail.com", "699999999", LocalDateTime.now(), null);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        Cuenta cuenta = new Cuenta(null, "ES-MILLONARIA", BigDecimal.ZERO, LocalDateTime.now(), clienteGuardado, null);

        cuentaRepository.save(cuenta);

        // Preparamos el JSON del depósito de 5000 euros

        OperacionDTO depositoDTO = new OperacionDTO();

        depositoDTO.setNumeroCuenta("ES-MILLONARIA");

        depositoDTO.setImporte(new BigDecimal("5000.00"));

        depositoDTO.setDescripcion("Premio de lotería");

        // 3. ACT: Lanzamos el HTTP POST real al Controlador

        mockMvc.perform(post("/api/operaciones/deposito")

                        .with(csrf())

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(depositoDTO)))

                .andExpect(status().isOk());

        // 4. ASSERT: Vamos a la base de datos a comprobar la verdad absoluta

        Cuenta cuentaActualizada = cuentaRepository.findByNumeroCuenta("ES-MILLONARIA").orElseThrow();

        // ¡El saldo debe ser 5000!

        assertThat(cuentaActualizada.getSaldo()).isEqualByComparingTo(new BigDecimal("5000.00"));

        // ¡Debe existir 1 movimiento registrado!

        assertThat(movimientoRepository.findAll()).hasSize(1);

        assertThat(movimientoRepository.findAll().get(0).getTipo().name()).isEqualTo("DEPOSITO");

    }

}
