package br.com.fiap.banco_tranquilo_worker.consumer;

import br.com.fiap.banco_tranquilo_worker.config.RabbitConfig;
import br.com.fiap.banco_tranquilo_worker.dto.request.PagamentoRequest;
import br.com.fiap.banco_tranquilo_worker.dto.response.PagamentoResponse;
import br.com.fiap.banco_tranquilo_worker.service.ContaService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PagamentoRequestConsumidor {

    private final ContaService contaService;
    private final AmqpTemplate rabbitTemplate;

    public PagamentoRequestConsumidor(ContaService contaService, AmqpTemplate rabbitTemplate) {
        this.contaService = contaService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitConfig.REQUEST_QUEUE)
    public void receber(PagamentoRequest request) {
        System.out.println("Banco Tranquilo recebeu pedido " + request.pedidoId()
                + " | " + request.email() + " | R$ " + request.valor());

        var decisao = contaService.cobrar(request.email(), request.valor());

        if (decisao.aprovado()) {
            System.out.println("==== APROVADO pedido " + request.pedidoId());
            rabbitTemplate.convertAndSend(RabbitConfig.SUCESSO_EXCHANGE, RabbitConfig.SUCESSO_KEY,
                    new PagamentoResponse(request.pedidoId(), "APROVADO", decisao.mensagem()));
        } else {
            System.out.println("==== RECUSADO pedido " + request.pedidoId() + " | " + decisao.mensagem());
            rabbitTemplate.convertAndSend(RabbitConfig.ERRO_EXCHANGE, RabbitConfig.ERRO_KEY,
                    new PagamentoResponse(request.pedidoId(), "RECUSADO", decisao.mensagem()));
        }
    }
}