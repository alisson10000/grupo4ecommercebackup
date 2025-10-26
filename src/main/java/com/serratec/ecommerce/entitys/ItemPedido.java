package com.serratec.ecommerce.entitys;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "itens_pedido")
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pedido é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_pedido",nullable = false)
    private Pedido pedido;

    @NotNull(message = "Produto é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_produto")
    private Produto produto;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    @NotNull(message = "Valor de venda é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valorVenda;

    @DecimalMin(value = "0.00", message = "Desconto não pode ser negativo")
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
        BigDecimal subtotal = valorVenda.multiply(new BigDecimal(quantidade));
        return subtotal.subtract(desconto);
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