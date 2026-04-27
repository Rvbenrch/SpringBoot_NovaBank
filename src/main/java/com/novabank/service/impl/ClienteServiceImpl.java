package com.novabank.service.impl;

import com.novabank.model.Cliente;
import com.novabank.repository.ClienteRepository;
import com.novabank.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente crearCliente(Cliente cliente) {

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Ya existe un cliente con el email: " + cliente.getEmail());
        }

        if (clienteRepository.existsByTelefono(cliente.getTelefono())) {
            throw new IllegalArgumentException("Ya existe un cliente con el teléfono: " + cliente.getTelefono());
        }

        log.info("Creando cliente con DNI {}", cliente.getDni());
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));
    }

    @Override
    public Cliente buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con DNI: " + dni));
    }

    @Override
    public boolean existeEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    @Override
    public boolean existeTelefono(String telefono) {
        return clienteRepository.existsByTelefono(telefono);
    }

    @Override
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
}