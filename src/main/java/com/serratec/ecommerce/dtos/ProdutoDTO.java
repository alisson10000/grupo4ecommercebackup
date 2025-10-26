package com.serratec.ecommerce.dtos;

import com.serratec.ecommerce.entitys.Produto;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para exibição de produtos com categoria resumida")
public class ProdutoDTO {

    @Schema(description = "Identificador único do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Notebook Gamer Lenovo Legion 5")
    private String nome;

    @Schema(description = "Descrição detalhada do produto", example = "Notebook com Ryzen 7, 16GB RAM e RTX 3060")
    private String descricao;

    @Schema(description = "Preço do produto em reais", example = "5499.90")
    private Double preco;

    @Schema(description = "Quantidade disponível em estoque", example = "25")
    private Integer quantidadeEstoque;

    @Schema(description = "Categoria resumida do produto")
    private CategoriaResumoDTO categoria;

    public ProdutoDTO() {}

    public ProdutoDTO(Long id, String nome, String descricao, Double preco, Integer quantidadeEstoque, CategoriaResumoDTO categoria) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.categoria = categoria;
    }

    // Construtor que converte da entidade
    public ProdutoDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.quantidadeEstoque = produto.getQuantidadeEstoque();

        if (produto.getCategoria() != null) {
            this.categoria = new CategoriaResumoDTO(
                produto.getCategoria().getId(),
                produto.getCategoria().getNome()
            );
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }

    public CategoriaResumoDTO getCategoria() { return categoria; }
    public void setCategoria(CategoriaResumoDTO categoria) { this.categoria = categoria; }
}
