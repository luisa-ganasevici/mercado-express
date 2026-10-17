package br.com.fiap.mercadoexpress.messaging;

import br.com.fiap.mercadoexpress.config.RabbitConfig;
import br.com.fiap.mercadoexpress.dto.request.PagamentoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoRequestProdutor {

    private final RabbitTemplate rabbitTemplate;

    public void enviar(PagamentoRequest request) {
        System.out.println("==== Mercado Express enviou pedido ao Banco: " + request);
        rabbitTemplate.convertAndSend(
                RabbitConfig.REQUEST_EXCHANGE, RabbitConfig.REQUEST_KEY, request);
    }
}