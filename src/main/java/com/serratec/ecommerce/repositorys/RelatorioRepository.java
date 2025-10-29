package com.serratec.ecommerce.repositorys;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.serratec.ecommerce.entitys.Produto;

@Repository
public interface RelatorioRepository extends JpaRepository<Produto, Long> {
	
	
	//relatorio de produtos por categoria
	@Query("SELECT c.nome AS categoria, COUNT(p) AS totalProdutos, AVG(p.preco) AS mediaPreco "
		     + "FROM Produto p JOIN p.categoria c "
		     + "GROUP BY c.nome")
		List<Object[]> relatorioPorCategoria();
		
	    //ranking dos produtos mais caros
	   
		List<Produto> findAllByOrderByPrecoDesc(Pageable pageable);
	    //valor total do estoque
	    @Query("SELECT SUM(p.preco * p.quantidadeEstoque) FROM Produto p")
	    Double valorTotalEstoque();
}
