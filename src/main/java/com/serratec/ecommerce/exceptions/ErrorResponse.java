package com.serratec.ecommerce.exceptions;

import java.time.LocalDateTime;

public class ErrorResponse {
	private String titulo;
    private String detalhes;
    private final LocalDateTime timestamp = LocalDateTime.now(); 

    public ErrorResponse(String titulo, String detalhes) {
        this.titulo = titulo;
        this.detalhes = detalhes;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
