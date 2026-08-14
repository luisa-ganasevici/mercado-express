package br.com.fiap.mercadoexpress.repository;

import br.com.fiap.mercadoexpress.entity.Mercado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MercadoRepository extends JpaRepository<Mercado, Long> {
}