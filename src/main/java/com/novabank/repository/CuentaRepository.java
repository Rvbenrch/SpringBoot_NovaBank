package com.novabank.repository;

import com.novabank.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    List<Cuenta> findByClienteId(Long clienteId);
    // Cargar cuenta + movimientos en una sola consulta (JPQL)
    @Query("SELECT c FROM Cuenta c LEFT JOIN FETCH c.movimientos WHERE c.id = :id")
    Optional<Cuenta> findByIdWithMovimientos(@Param("id") Long id);
}