package br.com.fiap.mercadoexpress.messaging;

import br.com.fiap.mercadoexpress.config.RabbitConfig;
import br.com.fiap.mercadoexpress.dto.response.PagamentoResponse;
import br.com.fiap.mercadoexpress.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoSucessoConsumidor {

    private final PedidoService pedidoService;

    @RabbitListener(queues = RabbitConfig.SUCESSO_QUEUE)
    public void receber(PagamentoResponse resposta) {
        System.out.println("==== Mercado Express recebeu SUCESSO: " + resposta);
        pedidoService.confirmarPagamento(resposta.pedidoId());
    }
}