package com.serratec.ecommerce.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusPedido {
    PENDENTE("Pendente"),
    PAGO("Pago"),
    ENVIADO("Enviado"),
    CANCELADO("Cancelado");

    private String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @JsonCreator
    public static StatusPedido fromString(String value) {
        for (StatusPedido status : StatusPedido.values()) {
            if (status.name().equalsIgnoreCase(value) ||
                    status.descricao.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + value);
    }
}