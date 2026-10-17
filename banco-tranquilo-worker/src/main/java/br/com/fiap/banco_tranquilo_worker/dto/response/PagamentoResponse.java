package br.com.fiap.banco_tranquilo_worker.dto.response;

public record PagamentoResponse(Long pedidoId, String status, String mensagem) {}