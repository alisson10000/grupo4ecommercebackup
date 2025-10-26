package com.serratec.ecommerce.repositorys;

import com.serratec.ecommerce.entitys.Pedido;
import com.serratec.ecommerce.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Long> {

    List<Pedido> findByClienteId(Long clienteId);
    List<Pedido> findByStatus(StatusPedido status);
}