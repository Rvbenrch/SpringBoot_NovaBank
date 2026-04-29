package com.novabank.service;

import com.novabank.dto.ClienteDTO;
import java.util.List;

public interface ClienteService {
    ClienteDTO crearCliente(ClienteDTO dto);
    ClienteDTO obtenerCliente(Long id);
    List<ClienteDTO> listarClientes();
}