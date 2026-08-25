package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.entity.Mercado;
import br.com.fiap.mercadoexpress.service.MercadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/mercado")
@RequiredArgsConstructor
public class MercadoController {

    private final MercadoService mercadoService;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Mercado>>> listarTodos() {
        List<EntityModel<Mercado>> mercados = mercadoService.listarTodos().stream()
                .map(this::paraEntityModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Mercado>> collectionModel = CollectionModel.of(mercados,
                linkTo(methodOn(MercadoController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mercado>> buscarPorId(@PathVariable Long id) {
        Mercado mercado = mercadoService.buscarPorId(id);
        return ResponseEntity.ok(paraEntityModel(mercado));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Mercado>> criar(@RequestBody Mercado mercado) {
        Mercado mercadoCriado = mercadoService.criar(mercado);

        EntityModel<Mercado> entityModel = paraEntityModel(mercadoCriado);

        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Mercado>> atualizar(@PathVariable Long id,
                                                          @RequestBody Mercado mercado) {
        Mercado mercadoAtualizado = mercadoService.atualizar(id, mercado);
        return ResponseEntity.ok(paraEntityModel(mercadoAtualizado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Mercado>> atualizarParcial(@PathVariable Long id, @RequestBody Mercado mercado) {
        Mercado mercadoAtualizado = mercadoService.atualizarParcial(id, mercado);
        return ResponseEntity.ok(paraEntityModel(mercadoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        mercadoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<Mercado> paraEntityModel(Mercado mercado) {
        return EntityModel.of(mercado,
                linkTo(methodOn(MercadoController.class).buscarPorId(mercado.getId())).withSelfRel(),
                linkTo(methodOn(MercadoController.class).listarTodos()).withRel("all-mercados"));
    }
}