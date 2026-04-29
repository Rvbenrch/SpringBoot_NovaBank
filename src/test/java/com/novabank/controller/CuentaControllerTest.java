package com.novabank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.novabank.dto.CuentaDTO;

import com.novabank.service.CuentaService;

import com.novabank.service.JwtService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CuentaController.class)

class CuentaControllerTest {

    @Autowired

    private MockMvc mockMvc;

    @Autowired

    private ObjectMapper objectMapper;

    @MockBean

    private CuentaService cuentaService;

    @MockBean

    private JwtService jwtService;

    @MockBean

    private UserDetailsService userDetailsService;

    @Test

    @WithMockUser

    void crearCuenta_debeRetornar201() throws Exception {

        CuentaDTO dtoEntrada = new CuentaDTO();

        dtoEntrada.setNumeroCuenta("ES12345");

        dtoEntrada.setClienteId(1L);

        CuentaDTO dtoSalida = new CuentaDTO();

        dtoSalida.setId(1L);

        dtoSalida.setNumeroCuenta("ES12345");

        dtoSalida.setSaldo(BigDecimal.ZERO);

        when(cuentaService.crearCuenta(any(CuentaDTO.class))).thenReturn(dtoSalida);

        mockMvc.perform(post("/api/cuentas")

                        .with(csrf())

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(dtoEntrada)))

                .andExpect(status().isCreated())

                .andExpect(jsonPath("$.numeroCuenta").value("ES12345"));

    }

    @Test

    @WithMockUser

    void consultarSaldo_debeRetornar200() throws Exception {

        when(cuentaService.obtenerSaldo(1L)).thenReturn(new BigDecimal("1500.50"));

        mockMvc.perform(get("/api/cuentas/1/saldo"))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$").value(1500.50));

    }

}
