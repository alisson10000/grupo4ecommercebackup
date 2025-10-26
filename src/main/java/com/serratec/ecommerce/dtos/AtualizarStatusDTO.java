package com.serratec.ecommerce.dtos;

import com.serratec.ecommerce.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public class AtualizarStatusDTO {

    @NotNull(message = "Status é obrigatório")
    private StatusPedido status;

    public AtualizarStatusDTO() {
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }
}

