package br.com.fiap.mercadoexpress.repository;

import br.com.fiap.mercadoexpress.entity.EmailLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<EmailLog, Long> {

    List<EmailLog> findAllByOrderByDataEnvioDesc();
}