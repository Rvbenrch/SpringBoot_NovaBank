package com.novabank.service.impl;

import com.novabank.dto.ClienteDTO;
import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.ValidacionException;
import com.novabank.mapper.ClienteMapper;
import com.novabank.model.Cliente;
import com.novabank.repository.ClienteRepository;
import com.novabank.service.ClienteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteDTO crearCliente(ClienteCreateDTO dto) {

        if (clienteRepository.findByDni(dto.getDni()).isPresent()) {
            throw new ValidacionException("Ya existe un cliente con el DNI: " + dto.getDni());
        }

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new ValidacionException("Ya existe un cliente con el email: " + dto.getEmail());
        }

        if (clienteRepository.existsByTelefono(dto.getTelefono())) {
            throw new ValidacionException("Ya existe un cliente con el teléfono: " + dto.getTelefono());
        }

        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);

        log.info("Cliente creado con id {}", guardado.getId());

        return ClienteMapper.toDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO obtenerCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cliente no encontrado con id: " + id)
                );

        return ClienteMapper.toDTO(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteMapper::toDTO)
                .collect(Collectors.toList());
    }
}