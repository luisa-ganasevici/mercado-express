    package br.com.fiap.mercadoexpress.exception;

    public class UsuarioNotFoundException extends RuntimeException {
        public UsuarioNotFoundException(Long id) {
            super("Usuario não encontrado com o id: " + id);
        }
    }
