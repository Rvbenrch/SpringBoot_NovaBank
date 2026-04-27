package com.novabank.service;

import com.novabank.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteService {

    Cliente crearCliente(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorDni(String dni);

    boolean existeEmail(String email);

    boolean existeTelefono(String telefono);

    List<Cliente> listarClientes();
}