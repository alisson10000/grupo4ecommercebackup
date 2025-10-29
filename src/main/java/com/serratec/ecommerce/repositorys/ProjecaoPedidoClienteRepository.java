package com.serratec.ecommerce.repositorys;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ProjecaoPedidoClienteRepository {
	String getNomeCliente();

	String getCpfCliente();

	String getEmailCliente();

	Long getIdPedido();

	LocalDateTime getDataPedido();

	String getStatusPedido();

	String getNomeProduto();

	Integer getQuantidade();

	BigDecimal getValorUnitario();

	BigDecimal getDesconto();

	BigDecimal getSubtotal();

	BigDecimal getTotalPedido();
}
