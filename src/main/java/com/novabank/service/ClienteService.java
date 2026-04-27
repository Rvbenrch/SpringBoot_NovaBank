package com.novabank.service;

import com.novabank.model.Cliente;

import java.util.List;

public interface ClienteService {

    Cliente crearCliente(Cliente cliente);

    Cliente buscarPorId(Long id);

    Cliente buscarPorDni(String dni);

    boolean existeEmail(String email);

    boolean existeTelefono(String telefono);

    List<Cliente> listarClientes();
}