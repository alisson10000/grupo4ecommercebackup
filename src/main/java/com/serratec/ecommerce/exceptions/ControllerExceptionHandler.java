package com.serratec.ecommerce.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	// erros de "Não Encontrado" (404)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(EntityNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("Recurso não encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // infos (401)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> badCredentials(BadCredentialsException ex) {
        ErrorResponse error = new ErrorResponse("Credenciais incorretas", "Email ou senha inválidos.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // validaçao (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex) {
        String detalhes = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ErrorResponse error = new ErrorResponse("Erro de validação", detalhes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // regras de negocio (400)
    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErrorResponse> businessRule(BusinessRuleException ex) {
        ErrorResponse error = new ErrorResponse("Requisição inválida", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // erro de enum (400)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> enumError(MethodArgumentTypeMismatchException ex) {
        String detalhes = "O valor '" + ex.getValue() + "' não é válido para o parâmetro '" + ex.getName() + "'.";
        ErrorResponse error = new ErrorResponse("Parâmetro inválido", detalhes);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // handler generico (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> generic(Exception ex) {
        ex.printStackTrace(); 
        ErrorResponse error = new ErrorResponse("Erro inesperado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
