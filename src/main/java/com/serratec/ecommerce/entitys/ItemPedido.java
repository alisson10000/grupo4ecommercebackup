package com.serratec.ecommerce.entitys;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "itens_pedido")
@Schema(description = "Entidade que representa um item de um pedido no sistema.")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do item do pedido.", example = "1")
    private Long id;

    @NotNull(message = "Pedido é obrigatório")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_pedido")
    @Schema(description = "Pedido ao qual este item pertence.")
    private Pedido pedido;

    @NotNull(message = "Produto é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_produto")
    @Schema(description = "Produto que compõe este item do pedido.")
    private Produto produto;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    @Schema(description = "Quantidade do produto no pedido.", example = "2")
    private Integer quantidade;

    @NotNull(message = "Valor de venda é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Schema(description = "Valor unitário de venda do produto.", example = "99.90")
    private BigDecimal valorVenda;

    @DecimalMin(value = "0.00", message = "Desconto não pode ser negativo")
    @Schema(description = "Valor do desconto aplicado ao item.", example = "10.00")
    private BigDecimal desconto = BigDecimal.ZERO;

    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Produto produto, Integer quantidade,
                      BigDecimal valorVenda, BigDecimal desconto) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorVenda = valorVenda;
        this.desconto = desconto != null ? desconto : BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }

    public BigDecimal getDesconto() {
        return desconto;
    }

    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = valorVenda.multiply(BigDecimal.valueOf(quantidade));
        return subtotal.subtract(desconto != null ? desconto : BigDecimal.ZERO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}