package br.com.fiap.mercadoexpress.service;

import br.com.fiap.mercadoexpress.dto.request.PagamentoRequest;
import br.com.fiap.mercadoexpress.entity.Mercado;
import br.com.fiap.mercadoexpress.entity.Pedido;
import br.com.fiap.mercadoexpress.entity.StatusPedido;
import br.com.fiap.mercadoexpress.entity.Usuario;
import br.com.fiap.mercadoexpress.messaging.PagamentoRequestProdutor;
import br.com.fiap.mercadoexpress.repository.MercadoRepository;
import br.com.fiap.mercadoexpress.repository.PedidoRepository;
import br.com.fiap.mercadoexpress.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MercadoRepository mercadoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagamentoRequestProdutor requestProdutor;

    public Pedido criarPedido(Mercado mercado, Integer quantidade, String username) {
        String email = usuarioRepository.findByUsername(username)
                .map(Usuario::getEmail)
                .orElse(null);

        BigDecimal qtd = BigDecimal.valueOf(quantidade);
        BigDecimal valorTotal = mercado.getPreco().multiply(qtd);
        BigDecimal custoTotal = mercado.getCusto() == null
                ? BigDecimal.ZERO
                : mercado.getCusto().multiply(qtd);

        Pedido pedido = new Pedido();
        pedido.setMercado(mercado);
        pedido.setUsername(username);
        pedido.setEmail(email);
        pedido.setQuantidade(quantidade);
        pedido.setValorTotal(valorTotal);
        pedido.setCustoTotal(custoTotal);
        pedido.setStatus(StatusPedido.PENDENTE);
        pedido = pedidoRepository.save(pedido);

        requestProdutor.enviar(new PagamentoRequest(pedido.getId(), valorTotal, email));
        return pedido;
    }

    @Transactional
    public Optional<Pedido> confirmarPagamento(Long pedidoId) {
        Optional<Pedido> opt = pedidoRepository.findById(pedidoId);
        if (opt.isEmpty() || opt.get().getStatus() != StatusPedido.PENDENTE) {
            return Optional.empty();
        }
        Pedido pedido = opt.get();
        Mercado mercado = pedido.getMercado();

        if (mercado.getEstoque() == null || mercado.getEstoque() < pedido.getQuantidade()) {
            pedido.setStatus(StatusPedido.RECUSADO);
            return Optional.of(pedidoRepository.save(pedido));
        }

        int novoEstoque = mercado.getEstoque() - pedido.getQuantidade();
        mercado.setEstoque(novoEstoque);
        if (novoEstoque <= 0) {
            mercado.setDisponivel(false);
        }
        mercadoRepository.save(mercado);

        pedido.setStatus(StatusPedido.PAGO);
        return Optional.of(pedidoRepository.save(pedido));
    }

    @Transactional
    public void recusarPagamento(Long pedidoId) {
        pedidoRepository.findById(pedidoId).ifPresent(pedido -> {
            if (pedido.getStatus() == StatusPedido.PENDENTE) {
                pedido.setStatus(StatusPedido.RECUSADO);
                pedidoRepository.save(pedido);
            }
        });
    }

    public List<Pedido> listarDoUsuario(String username) {
        return pedidoRepository.findByUsernameOrderByDataPedidoDesc(username);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllByOrderByDataPedidoDesc();
    }
}