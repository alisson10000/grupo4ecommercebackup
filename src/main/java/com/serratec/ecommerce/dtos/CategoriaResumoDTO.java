package com.serratec.ecommerce.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO resumido da categoria (usado em produtos, pedidos etc.)")
public class CategoriaResumoDTO {

    @Schema(description = "Identificador da categoria", example = "1")
    private Long id;

    @Schema(description = "Nome da categoria", example = "Eletrônicos")
    private String nome;

    public CategoriaResumoDTO() {}

    public CategoriaResumoDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
