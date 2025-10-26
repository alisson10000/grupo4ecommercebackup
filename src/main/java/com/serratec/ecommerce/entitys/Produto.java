package com.serratec.ecommerce.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "produto")
@Schema(description = "Entidade que representa os produtos disponíveis no sistema de e-commerce")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do produto", example = "1")
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório.")
    @Column(nullable = false, length = 100)
    @Schema(description = "Nome do produto", example = "Notebook Gamer Lenovo Legion 5")
    private String nome;

    @NotBlank(message = "A descrição do produto é obrigatória.")
    @Column(nullable = false, length = 500)
    @Schema(description = "Descrição detalhada do produto", example = "Notebook com processador AMD Ryzen 7, 16GB RAM, SSD 512GB e placa RTX 3060.")
    private String descricao;

    @NotNull(message = "O preço do produto é obrigatório.")
    @DecimalMin(value = "0.1", message = "O preço deve ser no mínimo 0.1")
    @Column(nullable = false)
    @Schema(description = "Preço do produto em reais", example = "5499.90")
    private Double preco;
    
    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    @Column(name = "quantidade_estoque", nullable = false)
    @Schema(description = "Quantidade disponível em estoque", example = "25")
    private Integer quantidadeEstoque;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @Schema(description = "Categoria à qual o produto pertence")
    private Categoria categoria;

    // Construtores
    public Produto() {}

    public Produto(Long id, String nome, String descricao, Double preco, Integer quantidadeEstoque, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.categoria = categoria;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
