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
        int reservado = mercadoRepository.reservarEstoque(mercado.getId(), quantidade);
        if (reservado == 0) {
            throw new IllegalStateException("Estoque insuficiente");
        }
        mercadoRepository.esconderSeZerado(mercado.getId(), false);

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

        try {
            requestProdutor.enviar(new PagamentoRequest(pedido.getId(), valorTotal, email));
        } catch (Exception e) {
            recusarPagamento(pedido.getId(), "Falha ao contatar o Banco Tranquilo");
        }
        return pedido;
    }

    @Transactional
    public Optional<Pedido> confirmarPagamento(Long pedidoId) {
        Optional<Pedido> opt = pedidoRepository.findById(pedidoId);
        if (opt.isEmpty() || opt.get().getStatus() != StatusPedido.PENDENTE) {
            return Optional.empty();
        }
        Pedido pedido = opt.get();
        pedido.setStatus(StatusPedido.PAGO);
        pedido.setMotivo("Pagamento aprovado");
        return Optional.of(pedidoRepository.save(pedido));
    }

    @Transactional
    public void recusarPagamento(Long pedidoId, String motivo) {
        Optional<Pedido> opt = pedidoRepository.findById(pedidoId);
        if (opt.isEmpty() || opt.get().getStatus() != StatusPedido.PENDENTE) {
            return;
        }
        Pedido pedido = opt.get();
        Long mercadoId = pedido.getMercado().getId();
        Integer qtd = pedido.getQuantidade();

        pedido.setStatus(StatusPedido.RECUSADO);
        pedido.setMotivo(motivo);
        pedidoRepository.saveAndFlush(pedido);

        mercadoRepository.devolverEstoque(mercadoId, qtd, true);
    }

    public List<Pedido> listarDoUsuario(String username) {
        return pedidoRepository.findByUsernameOrderByDataPedidoDesc(username);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllByOrderByDataPedidoDesc();
    }
}