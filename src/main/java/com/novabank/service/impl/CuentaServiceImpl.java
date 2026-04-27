package com.novabank.service.impl;

import com.novabank.exception.RecursoNoEncontradoException;
import com.novabank.exception.ValidacionException;
import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.model.Movimiento;
import com.novabank.model.TipoMovimiento;
import com.novabank.repository.ClienteRepository;
import com.novabank.repository.CuentaRepository;
import com.novabank.repository.MovimientoRepository;
import com.novabank.service.CuentaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimientoRepository movimientoRepository;

    @Override
    public Cuenta crearCuenta(Cuenta cuenta, Long clienteId) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cliente no encontrado con id: " + clienteId)
                );

        if (cuentaRepository.findByNumeroCuenta(cuenta.getNumeroCuenta()).isPresent()) {
            throw new ValidacionException("Ya existe una cuenta con el número: " + cuenta.getNumeroCuenta());
        }

        cuenta.setCliente(cliente);

        if (cuenta.getSaldo() == null) {
            cuenta.setSaldo(0.0);
        }

        log.info("Creando cuenta {} para cliente {}", cuenta.getNumeroCuenta(), clienteId);
        return cuentaRepository.save(cuenta);
    }

    @Override
    public Cuenta buscarPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada con id: " + id)
                );
    }

    @Override
    public Cuenta buscarPorNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada con número: " + numeroCuenta)
                );
    }

    @Override
    public List<Cuenta> listarPorCliente(Long clienteId) {

        if (!clienteRepository.existsById(clienteId)) {
            throw new RecursoNoEncontradoException("Cliente no encontrado con id: " + clienteId);
        }

        return cuentaRepository.findByClienteId(clienteId);
    }

    @Override
    public Cuenta cargarCuentaConMovimientos(Long id) {
        return cuentaRepository.findByIdWithMovimientos(id)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta no encontrada con id: " + id)
                );
    }

    @Override
    public boolean existeNumeroCuenta(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta).isPresent();
    }

    @Override
    @Transactional
    public void transferir(Long cuentaOrigenId, Long cuentaDestinoId, Double cantidad) {

        if (cantidad == null || cantidad <= 0) {
            throw new ValidacionException("La cantidad debe ser mayor que cero");
        }

        Cuenta origen = cuentaRepository.findById(cuentaOrigenId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta origen no encontrada con id: " + cuentaOrigenId)
                );

        Cuenta destino = cuentaRepository.findById(cuentaDestinoId)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Cuenta destino no encontrada con id: " + cuentaDestinoId)
                );

        if (origen.getSaldo() < cantidad) {
            throw new ValidacionException("Saldo insuficiente en la cuenta origen");
        }


        origen.setSaldo(origen.getSaldo() - cantidad);
        cuentaRepository.save(origen);


        destino.setSaldo(destino.getSaldo() + cantidad);
        cuentaRepository.save(destino);

        Movimiento movSalida = new Movimiento();
        movSalida.setTipo(TipoMovimiento.TRANSFERENCIA_SALIENTE);        movSalida.setCantidad(cantidad);
        movSalida.setCuenta(origen);
        movimientoRepository.save(movSalida);

        Movimiento movEntrada = new Movimiento();
        movEntrada.setTipo(TipoMovimiento.TRANSFERENCIA_ENTRANTE);        movEntrada.setCantidad(cantidad);
        movEntrada.setCuenta(destino);
        movimientoRepository.save(movEntrada);

        log.info("Transferencia realizada: {}€ de cuenta {} a cuenta {}",
                cantidad, cuentaOrigenId, cuentaDestinoId);
    }
}