package br.com.fiap.mercadoexpress.exception;

import br.com.fiap.mercadoexpress.controller.MercadoWebController;
import br.com.fiap.mercadoexpress.controller.UsuarioWebController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(assignableTypes = {MercadoWebController.class, UsuarioWebController.class})
public class WebExceptionHandler {

    @ExceptionHandler({MercadoNotFoundException.class, UsuarioNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNaoEncontrado(RuntimeException ex, Model model) {
        model.addAttribute("mensagem", ex.getMessage());
        return "error/404";
    }
}