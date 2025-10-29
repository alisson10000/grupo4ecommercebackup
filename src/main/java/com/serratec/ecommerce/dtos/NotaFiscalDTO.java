package com.serratec.ecommerce.dtos;


import com.serratec.ecommerce.repositorys.ProjecaoPedidoClienteRepository;

public class NotaFiscalDTO {

	private String nomeCliente;
	private String cpfCliente;
	private String emailCliente;
	private NotaFiscalPedidoDTO pedido;

	public NotaFiscalDTO(ProjecaoPedidoClienteRepository projecao) {
		this.nomeCliente = projecao.getNomeCliente();
		this.cpfCliente = projecao.getCpfCliente();
		this.emailCliente = projecao.getEmailCliente();
	}

	public String getNomeCliente() {
		return nomeCliente;
	}

	public void setNomeCliente(String nomeCliente) {
		this.nomeCliente = nomeCliente;
	}

	public String getCpfCliente() {
		return cpfCliente;
	}

	public void setCpfCliente(String cpfCliente) {
		this.cpfCliente = cpfCliente;
	}

	public String getEmailCliente() {
		return emailCliente;
	}

	public void setEmailCliente(String emailCliente) {
		this.emailCliente = emailCliente;
	}

	public NotaFiscalPedidoDTO getPedido() {
		return pedido;
	}

	public void setPedido(NotaFiscalPedidoDTO pedido) {
		this.pedido = pedido;
	}


}
