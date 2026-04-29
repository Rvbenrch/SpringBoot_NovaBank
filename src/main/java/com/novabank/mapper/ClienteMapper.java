package com.novabank.mapper;

import com.novabank.dto.ClienteDTO;
import com.novabank.model.Cliente;

public class ClienteMapper {

    public static ClienteDTO toDTO(Cliente cliente) {
        int numeroCuentas = (cliente.getCuentas() != null) ? cliente.getCuentas().size() : 0;

        return new ClienteDTO(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getApellidos(),
                cliente.getDni(),
                cliente.getEmail(),
                cliente.getTelefono(),
                cliente.getFechaCreacion(),
                numeroCuentas
        );
    }

    public static Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidos(dto.getApellidos());
        cliente.setDni(dto.getDni());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        return cliente;
    }
}