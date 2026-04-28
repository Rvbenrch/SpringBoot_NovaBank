package com.novabank.service.impl;

import com.novabank.dto.ClienteDTO;
import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.ValidacionException;
import com.novabank.model.Cliente;
import com.novabank.repository.ClienteRepository;
import com.novabank.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente crearCliente(Cliente cliente) {

        // Validación de DNI duplicado usando findByDni (no existsByDni)
        if (clienteRepository.findByDni(cliente.getDni()).isPresent()) {
            throw new ValidacionException("Ya existe un cliente con el DNI: " + cliente.getDni());
        }

        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ValidacionException("Ya existe un cliente con el email: " + cliente.getEmail());
        }

        if (clienteRepository.existsByTelefono(cliente.getTelefono())) {
            throw new ValidacionException("Ya existe un cliente con el teléfono: " + cliente.getTelefono());
        }

        log.info("Creando cliente con DNI {}", cliente.getDni());
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cliente no encontrado con id: " + id)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cliente no encontrado con DNI: " + dni)
                );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEmail(String email) {
        return clienteRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeTelefono(String telefono) {
        return clienteRepository.existsByTelefono(telefono);
    }

    @Override
    public ClienteDTO crearCliente(ClienteDTO dto) {
        return null;
    }

    @Override
    public ClienteDTO obtenerCliente(Long id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }
}