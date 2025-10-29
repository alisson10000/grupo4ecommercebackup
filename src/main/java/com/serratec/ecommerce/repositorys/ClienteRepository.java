package com.serratec.ecommerce.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.serratec.ecommerce.entitys.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	@Query(value = """
			select
			    c.nome as nome_cliente,
			    c.cpf as cpf_cliente,
			    c.email as email_cliente,
			    p.id as id_pedido,
			    p.data_pedido as data_pedido,
			    p.status as status_pedido,
			    pr.nome as nome_produto,
			    ip.quantidade as quantidade,
			    pr.preco as valor_unitario,
			    ip.desconto as desconto,
			    ip.valor_venda - ip.desconto as subtotal,
			    sum((ip.valor_venda) - ip.desconto) over (partition by p.id) AS total_pedido
			from
			    cliente c
			join
			    pedido p on c.id = p.id_cliente
			join
			    itens_pedido ip on p.id = ip.id_pedido
			join
			    produto pr on ip.id_produto = pr.id
			where
			    c.id = :clienteId and p.id = :pedidoId
			order by
			    p.data_pedido desc,
			    p.id,
			    pr.nome
			""", nativeQuery = true)
	List<ProjecaoPedidoClienteRepository> findDetalhesPedidosByClienteIdAndPedidoId(@Param("clienteId") Long clienteId,
			@Param("pedidoId") Long pedidoId);

	@Query(value = """
			select
			    c.id as id_cliente,
			    c.nome as nome_cliente,
			    c.cpf as cpf_cliente,
			    c.email as email_cliente,
			    sum((ip.valor_venda * ip.quantidade) - ip.desconto) as total_compras
			from
			    cliente c
			join
			    pedido p on c.id = p.id_cliente
			join
			    itens_pedido ip on p.id = ip.id_pedido
			group by
			    c.id, c.nome, c.cpf, c.email
			order by
			    total_compras desc
			limit 10
			""", nativeQuery = true)
	List<Object[]> findTop10ClientesPorValorTotalCompras();
}
