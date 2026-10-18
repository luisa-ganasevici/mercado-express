package br.com.fiap.mercadoexpress.messaging;

import br.com.fiap.mercadoexpress.config.RabbitConfig;
import br.com.fiap.mercadoexpress.dto.response.PagamentoResponse;
import br.com.fiap.mercadoexpress.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoErroConsumidor {

    private final PedidoService pedidoService;

    @RabbitListener(queues = RabbitConfig.ERRO_QUEUE)
    public void receber(PagamentoResponse resposta) {
        System.out.println("==== Mercado Express recebeu ERRO: " + resposta);
        pedidoService.recusarPagamento(resposta.pedidoId(), resposta.mensagem());
    }
}