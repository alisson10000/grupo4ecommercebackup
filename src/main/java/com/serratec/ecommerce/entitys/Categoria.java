package com.serratec.ecommerce.entitys;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Schema(name = "Categoria", description = "Representa uma categoria de produtos, como 'Eletrônicos', 'Roupas', 'Livros', etc.")
@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único da categoria", example = "1")
    private Long id;

    @NotBlank(message = "O nome da categoria é obrigatório.")
    @Size(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres.")
    @Column(nullable = false, unique = true, length = 100)
    @Schema(description = "Nome da categoria", example = "Eletrônicos")
    private String nome;

    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    @Column(length = 255)
    @Schema(description = "Descrição opcional da categoria", example = "Dispositivos e equipamentos eletrônicos em geral.")
    private String descricao;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de produtos vinculados a esta categoria")
    private List<Produto> produtos;

    //  Construtores
    public Categoria() {}

    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    //  Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public List<Produto> getProdutos() { return produtos; }
    public void setProdutos(List<Produto> produtos) { this.produtos = produtos; }
}
