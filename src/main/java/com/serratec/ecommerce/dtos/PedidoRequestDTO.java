package com.serratec.ecommerce.dtos;

import com.serratec.ecommerce.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class PedidoRequestDTO {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    private StatusPedido status;

    @NotNull(message = "Itens do pedido são obrigatórios")
    private List<ItemPedidoDTO> itens;

    public PedidoRequestDTO() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}