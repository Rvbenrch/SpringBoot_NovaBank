package com.novabank.controller;

import com.novabank.dto.ClienteCreateDTO;
import com.novabank.dto.ClienteDTO;
import com.novabank.mapper.ClienteMapper;
import com.novabank.model.Cliente;
import com.novabank.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteDTO> listarClientes() {
        return clienteService.listarClientes()
                .stream()
                .map(ClienteMapper::toDTO)
                .toList();
    }

    @PostMapping
    public ClienteDTO crearCliente(@RequestBody ClienteCreateDTO dto) {
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente creado = clienteService.crearCliente(cliente);
        return ClienteMapper.toDTO(creado);
    }

    @GetMapping("/{id}")
    public ClienteDTO buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ClienteMapper.toDTO(cliente);
    }

    @GetMapping("/dni/{dni}")
    public ClienteDTO buscarPorDni(@PathVariable String dni) {
        Cliente cliente = clienteService.buscarPorDni(dni);
        return ClienteMapper.toDTO(cliente);
    }
}