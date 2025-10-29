package com.serratec.ecommerce.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.serratec.ecommerce.repositorys.ProjecaoPedidoClienteRepository;

public class NotaFiscalPedidoDTO {
	private Long pedidoId;
	private LocalDateTime dataPedido;
	private String statusPedido;
	private List<NotaFiscalItemDTO> itens = new ArrayList<>();
	private BigDecimal valorTotalPedido = BigDecimal.ZERO;

	public NotaFiscalPedidoDTO(ProjecaoPedidoClienteRepository projecao) {
		this.pedidoId = projecao.getIdPedido();
		this.dataPedido = projecao.getDataPedido();
		this.statusPedido = projecao.getStatusPedido();
	}

	public void adicionarItem(NotaFiscalItemDTO item) {
		this.itens.add(item);
		this.valorTotalPedido = this.valorTotalPedido.add(item.getSubtotal());
	}

	public Long getPedidoId() {
		return pedidoId;
	}

	public void setPedidoId(Long pedidoId) {
		this.pedidoId = pedidoId;
	}

	public LocalDateTime getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(LocalDateTime dataPedido) {
		this.dataPedido = dataPedido;
	}

	public String getStatusPedido() {
		return statusPedido;
	}

	public void setStatusPedido(String statusPedido) {
		this.statusPedido = statusPedido;
	}

	public List<NotaFiscalItemDTO> getItens() {
		return itens;
	}

	public void setItens(List<NotaFiscalItemDTO> itens) {
		this.itens = itens;
	}

	public BigDecimal getValorTotalPedido() {
		return valorTotalPedido;
	}

	public void setValorTotalPedido(BigDecimal valorTotalPedido) {
		this.valorTotalPedido = valorTotalPedido;
	}

}
