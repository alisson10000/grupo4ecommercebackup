package com.serratec.ecommerce.repositorys;

import com.serratec.ecommerce.entitys.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    //  Busca produtos pelo nome (ignorando maiúsculas/minúsculas)
    List<Produto> findByNomeContainingIgnoreCase(String nome);

    //  Busca produtos por categoria (usando o ID da categoria)
    List<Produto> findByCategoriaId(Long categoriaId);

    // Busca produtos com estoque abaixo de determinado valor
    List<Produto> findByQuantidadeEstoqueLessThan(Integer quantidade);

    //  Busca produtos com preço abaixo de determinado valor
    List<Produto> findByPrecoLessThan(Double preco);
}
