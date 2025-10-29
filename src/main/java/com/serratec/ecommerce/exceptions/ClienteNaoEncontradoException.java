package com.serratec.ecommerce.exceptions;

public class ClienteNaoEncontradoException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ClienteNaoEncontradoException(Long id) {
        super("Cliente não encontrado com ID: " + id);
    }
}