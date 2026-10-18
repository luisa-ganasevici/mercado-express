package br.com.fiap.mercadoexpress.repository;

import br.com.fiap.mercadoexpress.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsernameOrderByDataPedidoDesc(String username);

    List<Pedido> findAllByOrderByDataPedidoDesc();
}