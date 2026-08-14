package br.com.fiap.mercadoexpress.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<EntityModel<Map<String, String>>> home() {
        Map<String, String> info = new LinkedHashMap<>();
        info.put("projeto", "Mercado Express API");
        info.put("descricao", "CP4 - Parte 1 (API e Deploy) - FIAP TDS");
        info.put("status", "no ar");
        info.put("observacao", "Esta e a Parte 1 do checkpoint. Endpoints adicionais e funcionalidades serao adicionados nas proximas etapas.");

        EntityModel<Map<String, String>> entityModel = EntityModel.of(info,
                linkTo(methodOn(MercadoController.class).listarTodos()).withRel("mercados"));

        return ResponseEntity.ok(entityModel);
    }
}