package com.serratec.ecommerce.dtos;

import java.math.BigDecimal;
import com.serratec.ecommerce.repositorys.ProjecaoPedidoClienteRepository;

public class NotaFiscalItemDTO {
	private String nomeProduto;
	private Integer quantidade;
	private BigDecimal valorUnitario;
	private BigDecimal desconto;
	private BigDecimal subtotal;
	private BigDecimal totalPedido;

	public NotaFiscalItemDTO(ProjecaoPedidoClienteRepository projecao) {
		super();
		this.nomeProduto = projecao.getNomeProduto();
		this.quantidade = projecao.getQuantidade();
		this.valorUnitario = projecao.getValorUnitario();
		this.desconto = projecao.getDesconto();
		this.subtotal = projecao.getSubtotal();
		this.totalPedido = projecao.getTotalPedido();

	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getDesconto() {
		return desconto;
	}

	public void setDesconto(BigDecimal desconto) {
		this.desconto = desconto;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getTotalPedido() {
		return totalPedido;
	}

	public void setTotalPedido(BigDecimal totalPedido) {
		this.totalPedido = totalPedido;
	}

}
