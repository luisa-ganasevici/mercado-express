package br.com.fiap.mercadoexpress.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@Controller
public class HomeController {

    @GetMapping("/")
    public String raiz() {
        return "redirect:/home";
    }

    @GetMapping("/api")
    public ResponseEntity<EntityModel<Map<String, String>>> home() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("projeto", "Mercado Express API");
        info.put("descricao", "CP5 - Parte I (Spring Security MVC e Deploy) - FIAP TDS");
        info.put("status", "no ar");
        info.put("observacao", "Aplicação web com login, perfis ADMIN/CLIENTE e CRUD de produtos.");
        EntityModel<Map<String, String>> entityModel = EntityModel.of(info,
                linkTo(methodOn(MercadoController.class).listarTodos()).withRel("mercados"));

        return ResponseEntity.ok(entityModel);
    }
}