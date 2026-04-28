package com.novabank.mapper;

import com.novabank.dto.ClienteCreateDTO;
import com.novabank.dto.ClienteDTO;
import com.novabank.model.Cliente;

public class ClienteMapper {

    public static Cliente toEntity(ClienteCreateDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidos(dto.getApellidos());
        cliente.setDni(dto.getDni());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        return cliente;
    }

    public static ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setId(cliente.getId());
        dto.setNombre(cliente.getNombre());
        dto.setApellidos(cliente.getApellidos());
        dto.setDni(cliente.getDni());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        dto.setFechaCreacion(cliente.getFechaCreacion());
        dto.setNumeroCuentas(
                cliente.getCuentas() != null ? cliente.getCuentas().size() : 0
        );
        return dto;
    }
}