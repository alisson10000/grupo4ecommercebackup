package com.serratec.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.serratec.ecommerce.dtos.ProdutoRankingDTO;
import com.serratec.ecommerce.dtos.ClienteRankingDTO;
import com.serratec.ecommerce.dtos.RelatorioDTO;
import com.serratec.ecommerce.services.RelatorioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/relatorios")
@Tag(name = "Relatórios", description = "Operações de relatórios da API")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/produtos-por-categoria")
    @Operation(summary = "Lista produtos por categoria")
    public ResponseEntity<List<RelatorioDTO>> produtosPorCategoria() {
        List<Object[]> resultados = relatorioService.gerarRelatorioPorCategoria();

        List<RelatorioDTO> relatorios = resultados.stream()
                .map(obj -> new RelatorioDTO(
                        (String) obj[0], // categoria
                        (Long) obj[1],   // totalProdutos
                        (Double) obj[2]  // mediaPreco
                ))
                .toList();

        return ResponseEntity.ok(relatorios);
    }

   
    @GetMapping("/ranking-produtos-caros")
    @Operation(summary = "Ranking dos produtos mais caros")
    public ResponseEntity<List<ProdutoRankingDTO>> rankingProdutosMaisCaros(
            @RequestParam(defaultValue = "15") int quantidade) {
        return ResponseEntity.ok(relatorioService.gerarRankingProdutosMaisCaros(quantidade));
    }

    @GetMapping("/valor-total-estoque")
    @Operation(summary = "Valor total do estoque")
    public ResponseEntity<Double> valorTotalEstoque() {
        return ResponseEntity.ok(relatorioService.calcularValorTotalEstoque());
    }

    @GetMapping("/top-clientes")
    @Operation(summary = "Top 10 clientes por valor total em compras")
    public ResponseEntity<List<ClienteRankingDTO>> topClientesPorValorTotalCompras() {
        return ResponseEntity.ok(relatorioService.obterTop10ClientesPorValorTotalCompras());
    }
}
