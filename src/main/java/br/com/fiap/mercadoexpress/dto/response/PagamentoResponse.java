package br.com.fiap.mercadoexpress.dto.response;

public record PagamentoResponse(Long pedidoId, String status, String mensagem) {}