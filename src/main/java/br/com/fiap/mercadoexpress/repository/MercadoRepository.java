package br.com.fiap.mercadoexpress.repository;

import br.com.fiap.mercadoexpress.entity.Mercado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MercadoRepository extends JpaRepository<Mercado, Long> {

    List<Mercado> findByNomeContainingIgnoreCase(String nome);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query("UPDATE Mercado m SET m.estoque = m.estoque - :qtd WHERE m.id = :id AND m.estoque >= :qtd")
    int reservarEstoque(@Param("id") Long id, @Param("qtd") Integer qtd);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query("UPDATE Mercado m SET m.estoque = m.estoque + :qtd, m.disponivel = :disp WHERE m.id = :id")
    int devolverEstoque(@Param("id") Long id, @Param("qtd") Integer qtd, @Param("disp") Boolean disp);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Transactional
    @Query("UPDATE Mercado m SET m.disponivel = :disp WHERE m.id = :id AND m.estoque <= 0")
    int esconderSeZerado(@Param("id") Long id, @Param("disp") Boolean disp);
}