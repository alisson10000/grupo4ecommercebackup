package com.serratec.ecommerce.exceptions;

//para os erros da regra de negocio (cpf duplicado, erro viacep e etc)
public class BusinessRuleException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessRuleException(String msg) {
		super(msg);
	}

}
