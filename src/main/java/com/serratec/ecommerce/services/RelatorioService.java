package com.serratec.ecommerce.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.serratec.ecommerce.dtos.ClienteRankingDTO;
import com.serratec.ecommerce.dtos.ProdutoRankingDTO;
import com.serratec.ecommerce.repositorys.ClienteRepository;
import com.serratec.ecommerce.repositorys.RelatorioRepository;

@Service
public class RelatorioService {
	
	@Autowired
	private RelatorioRepository relatorioRepository;

	@Autowired
	private ClienteRepository clienteRepository;
	
	public List<Object[]> gerarRelatorioPorCategoria() {
        return relatorioRepository.relatorioPorCategoria();
    }
	
	public List<ProdutoRankingDTO> gerarRankingProdutosMaisCaros(int quantidade) {
	    Pageable topN = PageRequest.of(0, quantidade);
	    return relatorioRepository.findAllByOrderByPrecoDesc(topN)
	            .stream()
	            .map(p -> new ProdutoRankingDTO(p.getNome(), p.getPreco()))
	            .toList();
	}

    public Double calcularValorTotalEstoque() {
        return relatorioRepository.valorTotalEstoque();
    }

	public List<ClienteRankingDTO> obterTop10ClientesPorValorTotalCompras() {
		return clienteRepository.findTop10ClientesPorValorTotalCompras()
				.stream()
				.map(row -> new ClienteRankingDTO(
						((Number) row[0]).longValue(),
						(String) row[1],
						(String) row[2],
						(String) row[3],
						row[4] != null ? ((Number) row[4]).doubleValue() : 0.0
				))
				.toList();
	}

}