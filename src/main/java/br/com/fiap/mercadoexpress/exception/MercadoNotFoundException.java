package br.com.fiap.mercadoexpress.exception;

public class MercadoNotFoundException extends RuntimeException {
    public MercadoNotFoundException(Long id) {
        super("Mercado não encontrado com id: " + id);
    }
}