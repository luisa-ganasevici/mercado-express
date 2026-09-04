package br.com.fiap.mercadoexpress.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecurityController {

    @GetMapping("/home/privado")
    public String rotaPrivada(){
        return "logado";
    }

    @GetMapping("/home/publico")
    @ResponseBody
    public String rotaPublica(){
        return "<h1>Rota publica</h1>";
    }

    @GetMapping("/home")
    public String retornohtml() { return "index"; }

    @GetMapping("/acesso-negado")
    public String acessoNegado(){
        return "acesso-negado";
    }
}