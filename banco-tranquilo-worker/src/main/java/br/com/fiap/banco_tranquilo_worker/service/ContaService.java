package br.com.fiap.banco_tranquilo_worker.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaService {

    public record Decisao(boolean aprovado, String mensagem) {}

    private final JdbcTemplate jdbc;

    public ContaService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Transactional
    public Decisao cobrar(String email, BigDecimal valor) {
        if (email == null || email.isBlank()) {
            return new Decisao(false, "Pedido sem e-mail do cliente");
        }

        List<BigDecimal> saldos = jdbc.queryForList(
                "SELECT SALDO FROM TDS_TB_CONTA_BANCO WHERE LOWER(EMAIL) = LOWER(?) FOR UPDATE",
                BigDecimal.class, email);

        if (saldos.isEmpty()) {
            return new Decisao(false, "Conta nao encontrada no Banco Tranquilo");
        }

        BigDecimal saldo = saldos.get(0);
        if (saldo.compareTo(valor) < 0) {
            return new Decisao(false, "Saldo insuficiente");
        }

        jdbc.update(
                "UPDATE TDS_TB_CONTA_BANCO SET SALDO = SALDO - ? WHERE LOWER(EMAIL) = LOWER(?)",
                valor, email);

        return new Decisao(true, "Pagamento aprovado");
    }
}