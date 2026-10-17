package br.com.fiap.mercadoexpress.dto.request;

import java.math.BigDecimal;

public record PagamentoRequest(Long pedidoId, BigDecimal valor, String email) {}