package com.novabank.service.impl;
import com.novabank.dto.ClienteDTO;
import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.ValidacionException;
import com.novabank.model.Cliente;
import com.novabank.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {
    // @Mock crea un "doble de riesgo" (un simulacro) de la base de datos
    @Mock
    private ClienteRepository clienteRepository;
    // @InjectMocks mete ese simulacro dentro de nuestro servicio real
    @InjectMocks
    private ClienteServiceImpl clienteService;
    @Test
    void obtenerCliente_cuandoExiste_debeRetornarDTO() {
        // 1. ARRANGE (Preparar el escenario)
        Cliente clienteSimulado = new Cliente(1L, "Ana", "Lopez", "11111111A", "ana@mail.com", "600000001", LocalDateTime.now(), new ArrayList<>());
        // Le decimos a nuestro mock: "Cuando alguien busque el ID 1, devuelve a Ana"
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteSimulado));
        // 2. ACT (Ejecutar la acción real)
        ClienteDTO resultado = clienteService.obtenerCliente(1L);
        // 3. ASSERT (Comprobar que el resultado es el esperado)
        assertThat(resultado.getNombre()).isEqualTo("Ana");
        assertThat(resultado.getDni()).isEqualTo("11111111A");
        // Verificamos que el repositorio fue llamado exactamente una vez
        verify(clienteRepository, times(1)).findById(1L);
    }
    @Test
    void obtenerCliente_cuandoNoExiste_debeLanzarExcepcion() {
        // 1. ARRANGE
        // Le decimos al mock: "Cuando busquen el ID 99, devuelve vacío (no encontrado)"
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());
        // 2 & 3. ACT & ASSERT (En casos de error, se hacen juntos)
        assertThatThrownBy(() -> clienteService.obtenerCliente(99L))
                .isInstanceOf(RecursoNoEncontradoException.class)
                .hasMessageContaining("99");
    }
    @Test
    void crearCliente_cuandoDniYaExiste_debeLanzarExcepcion() {
        // 1. ARRANGE
        ClienteDTO dtoNuevo = new ClienteDTO(null, "Angel", "Osakar", "10908099F", "angel@mail.com", "898236477", null, 0);
        // Simulamos que la base de datos ya tiene a alguien con ese DNI
        when(clienteRepository.findByDni("10908099F")).thenReturn(Optional.of(new Cliente()));
        // 2 & 3. ACT & ASSERT
        assertThatThrownBy(() -> clienteService.crearCliente(dtoNuevo))
                .isInstanceOf(ValidacionException.class)
                .hasMessageContaining("Ya existe un cliente con el DNI");
    }
    @Test
    void crearCliente_cuandoEmailYaExiste_debeLanzarExcepcion() {
        // 1. ARRANGE
        ClienteDTO dtoNuevo = new ClienteDTO(null, "Angel", "Osakar", "10908099F", "angel@mail.com", "898236477", null, 0);
        // El DNI pasa (no existe), pero el email sí existe en base de datos
        when(clienteRepository.findByDni(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.existsByEmail("angel@mail.com")).thenReturn(true);
        // 2 & 3. ACT & ASSERT
        assertThatThrownBy(() -> clienteService.crearCliente(dtoNuevo))
                .isInstanceOf(ValidacionException.class)
                .hasMessageContaining("Ya existe un cliente con el email");
    }
    @Test
    void crearCliente_cuandoTelefonoYaExiste_debeLanzarExcepcion() {
        // 1. ARRANGE
        ClienteDTO dtoNuevo = new ClienteDTO(null, "Angel", "Osakar", "10908099F", "angel@mail.com", "898236477", null, 0);
        // DNI y Email pasan, pero el Teléfono ya existe
        when(clienteRepository.findByDni(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.existsByTelefono("898236477")).thenReturn(true);
        // 2 & 3. ACT & ASSERT
        assertThatThrownBy(() -> clienteService.crearCliente(dtoNuevo))
                .isInstanceOf(ValidacionException.class)
                .hasMessageContaining("Ya existe un cliente con el teléfono");
    }
    @Test
    void crearCliente_cuandoTodoEsCorrecto_debeRetornarClienteCreado() {
        // 1. ARRANGE
        ClienteDTO dtoNuevo = new ClienteDTO(null, "Angel", "Osakar", "10908099F", "angel@mail.com", "898236477", null, 0);
        // Simulamos que no hay conflictos en la base de datos
        when(clienteRepository.findByDni(anyString())).thenReturn(Optional.empty());
        when(clienteRepository.existsByEmail(anyString())).thenReturn(false);
        when(clienteRepository.existsByTelefono(anyString())).thenReturn(false);
        // Simulamos lo que nos devolvería la base de datos al guardar (nos devuelve la entidad con un ID generado)
        Cliente clienteGuardado = new Cliente(1L, "Angel", "Osakar", "10908099F", "angel@mail.com", "898236477", LocalDateTime.now(), new ArrayList<>());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);
        // 2. ACT
        ClienteDTO resultado = clienteService.crearCliente(dtoNuevo);
        // 3. ASSERT
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L); // Comprobamos que le ha asignado el ID
        assertThat(resultado.getNombre()).isEqualTo("Angel");
    }
    @Test
    void listarClientes_debeRetornarListaDeDTOs() {
        // 1. ARRANGE
        Cliente c1 = new Cliente(1L, "Ana", "Lopez", "11111111A", "ana@mail.com", "600000001", LocalDateTime.now(), new ArrayList<>());
        Cliente c2 = new Cliente(2L, "Luis", "Gomez", "22222222B", "luis@mail.com", "600000002", LocalDateTime.now(), new ArrayList<>());
        when(clienteRepository.findAll()).thenReturn(java.util.List.of(c1, c2));
        // 2. ACT
        java.util.List<ClienteDTO> resultado = clienteService.listarClientes();
        // 3. ASSERT
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Ana");
        assertThat(resultado.get(1).getNombre()).isEqualTo("Luis");
    }
}