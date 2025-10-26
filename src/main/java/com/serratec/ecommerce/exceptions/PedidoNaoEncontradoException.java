package com.serratec.ecommerce.exceptions;

public class PedidoNaoEncontradoException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PedidoNaoEncontradoException(Long id) {
        super("Pedido não encontrado com ID: " + id);
    }
}