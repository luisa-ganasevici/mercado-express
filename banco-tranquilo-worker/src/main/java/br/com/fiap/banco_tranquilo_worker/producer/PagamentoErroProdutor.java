package br.com.fiap.banco_tranquilo_worker.producer;

import br.com.fiap.banco_tranquilo_worker.config.RabbitConfig;
import br.com.fiap.banco_tranquilo_worker.dto.response.PagamentoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoErroProdutor {

    private final RabbitTemplate rabbitTemplate;

    public void enviar(PagamentoResponse resposta) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.ERRO_EXCHANGE, RabbitConfig.ERRO_KEY, resposta);
    }
}