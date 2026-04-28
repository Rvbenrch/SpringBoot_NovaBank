package com.novabank.service;

import com.novabank.dto.OperacionDTO;
import com.novabank.dto.TransferenciaDTO;

public interface OperacionService {

    void realizarDeposito(OperacionDTO dto);

    void realizarRetiro(OperacionDTO dto);

    void realizarTransferencia(TransferenciaDTO dto);
}