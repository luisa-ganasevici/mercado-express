package br.com.fiap.mercadoexpress.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityTestController {

@GetMapping(name = "/publico")
    String rotaPublica(){
    return "<h1>Rota publica <h1>";
}


    @GetMapping(name = "/privado ")
        String rotaPrivada (){
    return "<h1>Rota privada <h1>";
    }



}
