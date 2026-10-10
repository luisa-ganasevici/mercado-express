package br.com.fiap.mercadoexpress.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    @GetMapping("/home/privado")
    public String rotaPrivada() {
        return "logado";
    }

    @GetMapping("/home")
    public String retornohtml() {
        return "index";
    }

    @GetMapping("/acesso-negado")
    public String acessoNegado() {
        return "acesso-negado";
    }

    @GetMapping("/admin/painel")
    public String painelAdmin() {
        return "painel-admin";
    }
}