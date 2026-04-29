package com.novabank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.novabank.dto.OperacionDTO;

import com.novabank.dto.TransferenciaDTO;

import com.novabank.service.JwtService;

import com.novabank.service.OperacionService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OperacionController.class)

class OperacionControllerTest {

    @Autowired

    private MockMvc mockMvc;

    @Autowired

    private ObjectMapper objectMapper;

    @MockBean

    private OperacionService operacionService;

    @MockBean

    private JwtService jwtService;

    @MockBean

    private UserDetailsService userDetailsService;

    @Test

    @WithMockUser

    void realizarDeposito_debeRetornar200() throws Exception {

        OperacionDTO dto = new OperacionDTO();

        dto.setNumeroCuenta("ES123");

        dto.setImporte(new BigDecimal("500.00"));

        mockMvc.perform(post("/api/operaciones/deposito")

                        .with(csrf())

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(dto)))

                .andExpect(status().isOk());

    }

    @Test

    @WithMockUser

    void realizarTransferencia_debeRetornar200() throws Exception {

        TransferenciaDTO dto = new TransferenciaDTO();

        dto.setCuentaOrigen("ES111");

        dto.setCuentaDestino("ES222");

        dto.setImporte(new BigDecimal("100.00"));

        mockMvc.perform(post("/api/operaciones/transferencia")

                        .with(csrf())

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(dto)))

                .andExpect(status().isOk());

    }

}
