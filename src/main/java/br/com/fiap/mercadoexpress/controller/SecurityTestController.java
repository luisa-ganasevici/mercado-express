package br.com.fiap.mercadoexpress.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class SecurityTestController {

    @GetMapping("/publico")
    @ResponseBody
        String rotaPublica(){
    return "<h1>Rota publica </h1>";
}


    @GetMapping("/privado")
    @ResponseBody
        String rotaPrivada (){
    return "<h1>Rota privada </h1>";
    }

    @GetMapping("/home")
        String retornohtml() { return "index"; }


}
