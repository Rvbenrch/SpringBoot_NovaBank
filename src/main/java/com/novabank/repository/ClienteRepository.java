package com.novabank.repository;

import com.novabank.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByDni(String dni);

    boolean existsByEmail(String email);

    boolean existsByTelefono(String telefono);

    boolean existsByDNI(String dni);

    Optional<Cliente> findByEmail(String email);
}