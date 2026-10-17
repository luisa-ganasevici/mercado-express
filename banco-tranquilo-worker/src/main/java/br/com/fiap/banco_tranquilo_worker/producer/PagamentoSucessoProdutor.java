package br.com.fiap.banco_tranquilo_worker.producer;

import br.com.fiap.banco_tranquilo_worker.config.RabbitConfig;
import br.com.fiap.banco_tranquilo_worker.dto.response.PagamentoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoSucessoProdutor {

    private final RabbitTemplate rabbitTemplate;

    public void enviar(PagamentoResponse resposta) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.SUCESSO_EXCHANGE, RabbitConfig.SUCESSO_KEY, resposta);
    }
}