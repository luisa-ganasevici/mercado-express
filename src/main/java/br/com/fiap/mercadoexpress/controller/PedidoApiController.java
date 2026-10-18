package br.com.fiap.mercadoexpress.controller;

import br.com.fiap.mercadoexpress.entity.Mercado;
import br.com.fiap.mercadoexpress.entity.Pedido;
import br.com.fiap.mercadoexpress.repository.PedidoRepository;
import br.com.fiap.mercadoexpress.service.MercadoService;
import br.com.fiap.mercadoexpress.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoApiController {

    public record PedidoRequest(Long produtoId, Integer quantidade) {}

    public record PedidoResponse(Long id, String produto, Integer quantidade,
                                 BigDecimal valorTotal, String status, String motivo) {}

    private final PedidoService pedidoService;
    private final MercadoService mercadoService;
    private final PedidoRepository pedidoRepository;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody PedidoRequest req, Authentication auth) {
        if (req.produtoId() == null || req.quantidade() == null || req.quantidade() < 1) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Informe produtoId e quantidade >= 1"));
        }
        Mercado mercado = mercadoService.buscarPorId(req.produtoId());
        try {
            Pedido pedido = pedidoService.criarPedido(mercado, req.quantidade(), auth.getName());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(paraResponse(pedido));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Produto indisponivel ou estoque insuficiente"));
        }
    }

    @GetMapping
    public List<PedidoResponse> listar(Authentication auth) {
        return pedidoService.listarDoUsuario(auth.getName()).stream().map(this::paraResponse).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscar(@PathVariable Long id, Authentication auth) {
        return pedidoRepository.findById(id)
                .filter(p -> auth.getName().equals(p.getUsername()))
                .map(p -> ResponseEntity.ok(paraResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    private PedidoResponse paraResponse(Pedido p) {
        return new PedidoResponse(p.getId(), p.getMercado().getNome(), p.getQuantidade(),
                p.getValorTotal(), p.getStatus().name(), p.getMotivo());
    }
}