package com.novabank.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novabank.dto.ClienteDTO;
import com.novabank.dto.CuentaDTO;
import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.ValidacionException;
import com.novabank.service.ClienteService;
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
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {
    @Autowired
    private MockMvc mockMvc; // Nuestro Postman fantasma
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ClienteService clienteService;
    @MockBean
    private CuentaService cuentaService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    // 1. GET LISTA (200 OK)
    @Test
    @WithMockUser
    void listarClientes_debeRetornar200ConLista() throws Exception {
        ClienteDTO cliente = new ClienteDTO(1L, "Ana", "Lopez", "11111111A", "ana@mail.com", "600000001", null, 0);
        when(clienteService.listarClientes()).thenReturn(List.of(cliente));
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ana"))
                .andExpect(jsonPath("$[0].dni").value("11111111A"));
    }
    // 2. POST CREAR (201 Created) -> ¡ARREGLADO EL ERROR 403!
    @Test
    @WithMockUser
    void crearCliente_debeRetornar201() throws Exception {
        ClienteDTO dtoEntrada = new ClienteDTO(null, "Carlos", "Sainz", "55555555C", "carlos@mail.com", "655555555", null, 0);
        ClienteDTO dtoSalida = new ClienteDTO(2L, "Carlos", "Sainz", "55555555C", "carlos@mail.com", "655555555", null, 0);
        when(clienteService.crearCliente(any(ClienteDTO.class))).thenReturn(dtoSalida);
        mockMvc.perform(post("/api/clientes")
                        .with(csrf()) // <--- AQUÍ ESTÁ LA MAGIA QUE SOLUCIONA EL 403
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoEntrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }
    // 3. GET SIN AUTORIZACION (401 Unauthorized) -> Intruso
    @Test
    void listarClientes_sinAutenticacion_debeRetornar401() throws Exception {
        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isUnauthorized());
    }
    // 4. POST CREAR CON DNI DUPLICADO (400 Bad Request)
    @Test
    @WithMockUser
    void crearCliente_conDniDuplicado_debeRetornar400() throws Exception {
        ClienteDTO dtoEntrada = new ClienteDTO(null, "Mala", "Persona", "11111111A", "malo@mail.com", "600000000", null, 0);
        // Simulamos que el servicio lanza la excepción que tú creaste
        when(clienteService.crearCliente(any(ClienteDTO.class)))
                .thenThrow(new ValidacionException("Ya existe un cliente con el DNI"));
        mockMvc.perform(post("/api/clientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtoEntrada)))
                .andExpect(status().isBadRequest()) // Esperamos un error 400
                .andExpect(jsonPath("$.mensaje").value("Ya existe un cliente con el DNI"));
    }
    // 5. GET OBTENER POR ID (200 OK)
    @Test
    @WithMockUser
    void obtenerCliente_cuandoExiste_debeRetornar200() throws Exception {
        ClienteDTO dtoSalida = new ClienteDTO(1L, "Ana", "Lopez", "11111111A", "ana@mail.com", "600000001", null, 0);
        when(clienteService.obtenerCliente(1L)).thenReturn(dtoSalida);
        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }
    // 6. GET OBTENER POR ID NO EXISTE (404 Not Found)
    @Test
    @WithMockUser
    void obtenerCliente_cuandoNoExiste_debeRetornar404() throws Exception {
        when(clienteService.obtenerCliente(99L))
                .thenThrow(new RecursoNoEncontradoException("Cliente no encontrado con id: 99"));
        mockMvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound()) // Esperamos un 404
                .andExpect(jsonPath("$.mensaje").value("Cliente no encontrado con id: 99"));
    }
    // 7. GET LISTAR CUENTAS DE CLIENTE (200 OK)
    @Test
    @WithMockUser
    void listarCuentasPorCliente_debeRetornar200() throws Exception {
        CuentaDTO cuenta = new CuentaDTO();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("ES1234567890");
        cuenta.setSaldo(new BigDecimal("100.0"));
        cuenta.setClienteId(1L);
        when(cuentaService.listarCuentasPorCliente(1L)).thenReturn(List.of(cuenta));
        mockMvc.perform(get("/api/clientes/1/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCuenta").value("ES1234567890"));
    }
}