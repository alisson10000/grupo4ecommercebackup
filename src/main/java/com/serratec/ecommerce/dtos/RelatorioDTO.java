package com.serratec.ecommerce.dtos;

public class RelatorioDTO {
	
	private String categoria;
	private Long totalProdutos;
	private Double mediaPreco;
	
	public RelatorioDTO(String categoria, Long totalProdutos, Double mediaPreco) {
		this.categoria = categoria;
		this.totalProdutos = totalProdutos;
		this.mediaPreco = mediaPreco;
	}
	
	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Long getTotalProdutos() {
		return totalProdutos;
	}

	public void setTotalProdutos(Long totalProdutos) {
		this.totalProdutos = totalProdutos;
	}

	public Double getMediaPreco() {
		return mediaPreco;
	}

	public void setMediaPreco(Double mediaPreco) {
		this.mediaPreco = mediaPreco;
	}
}