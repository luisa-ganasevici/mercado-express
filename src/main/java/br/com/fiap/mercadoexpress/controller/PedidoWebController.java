package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PedidoWebController {

    private final PedidoService pedidoService;

    @GetMapping("/meus-pedidos")
    public String meusPedidos(Authentication authentication, Model model) {
        model.addAttribute("pedidos", pedidoService.listarDoUsuario(authentication.getName()));
        return "meus-pedidos";
    }
}