package com.novabank.integration;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.novabank.dto.ClienteDTO;

import com.novabank.model.Cliente;

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

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest

@AutoConfigureMockMvc

@ActiveProfiles("test")

@Transactional

class ClienteIntegrationTest {

    @Autowired

    private MockMvc mockMvc;

    @Autowired

    private ObjectMapper objectMapper;

    @Autowired

    private ClienteRepository clienteRepository;

    // INYECTAMOS LOS OTROS REPOSITORIOS PARA PODER LIMPIARLOS

    @Autowired

    private CuentaRepository cuentaRepository;

    @Autowired

    private MovimientoRepository movimientoRepository;

    @Test

    @WithMockUser

    void flujoCompleto_crearCliente_seGuardaEnBaseDeDatosRealmente() throws Exception {



        ClienteDTO nuevoCliente = new ClienteDTO(null, "Fernando", "Alonso", "33333333A", "nano@mail.com", "633333333", null, 0);

        movimientoRepository.deleteAll();

        cuentaRepository.deleteAll();

        clienteRepository.deleteAll();


        assertThat(clienteRepository.findAll()).isEmpty();


        mockMvc.perform(post("/api/clientes")

                        .with(csrf())

                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(nuevoCliente)))

                .andExpect(status().isCreated());

        // 3. ASSERT: ¡La prueba definitiva!

        assertThat(clienteRepository.findAll()).hasSize(1);

        Cliente clienteGuardadoEnBD = clienteRepository.findAll().get(0);

        assertThat(clienteGuardadoEnBD.getNombre()).isEqualTo("Fernando");

        assertThat(clienteGuardadoEnBD.getDni()).isEqualTo("33333333A");

    }

}
