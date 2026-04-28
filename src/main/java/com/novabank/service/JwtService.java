package com.novabank.service;

public interface JwtService {

    String generarToken(String username);

    String extraerUsername(String token);

    boolean esTokenValido(String token, String username);
}