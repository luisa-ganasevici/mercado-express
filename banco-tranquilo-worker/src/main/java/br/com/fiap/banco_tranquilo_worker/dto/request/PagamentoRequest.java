package br.com.fiap.banco_tranquilo_worker.dto.request;

import java.math.BigDecimal;

public record PagamentoRequest(Long pedidoId, BigDecimal valor, String email) {}