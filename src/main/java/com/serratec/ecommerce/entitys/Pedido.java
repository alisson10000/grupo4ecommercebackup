package com.serratec.ecommerce.entitys;


import com.serratec.ecommerce.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "pedido")
@Schema(description = "Entidade que representa um pedido no sistema.")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único do pedido.", example = "1")
    private Long id;

    @NotNull(message = "Data do pedido é obrigatória")
    @Schema(description = "Data e hora em que o pedido foi realizado.", example = "2024-01-15T10:30:00")
    private LocalDateTime dataPedido;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @Schema(description = "Cliente que realizou o pedido.")
    private Cliente cliente;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Schema(description = "Status atual do pedido.", example = "PENDENTE")
    private StatusPedido status;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Schema(description = "Lista de itens que compõem o pedido.")
    private List<ItemPedido> itens = new ArrayList<>();

    public Pedido() {
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
    }

    public Pedido(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataPedido() {
        return dataPedido;
    }

    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public void adicionarItem(Produto produto, Integer quantidade,
                              BigDecimal desconto) {
        ItemPedido item = new ItemPedido(this, produto, quantidade, 
                BigDecimal.valueOf(produto.getPreco()), desconto);
        itens.add(item);
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
        item.setPedido(null);
    }

    public void limparItens() {
        itens.clear();
    }

    public BigDecimal calcularTotal() {
        return itens.stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}