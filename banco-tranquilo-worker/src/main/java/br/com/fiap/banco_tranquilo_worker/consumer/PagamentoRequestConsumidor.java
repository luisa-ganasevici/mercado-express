package br.com.fiap.banco_tranquilo_worker.consumer;

import br.com.fiap.banco_tranquilo_worker.config.RabbitConfig;
import br.com.fiap.banco_tranquilo_worker.dto.request.PagamentoRequest;
import br.com.fiap.banco_tranquilo_worker.dto.response.PagamentoResponse;
import br.com.fiap.banco_tranquilo_worker.producer.PagamentoErroProdutor;
import br.com.fiap.banco_tranquilo_worker.producer.PagamentoSucessoProdutor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class PagamentoRequestConsumidor {

    private final PagamentoSucessoProdutor sucessoProdutor;
    private final PagamentoErroProdutor erroProdutor;
    private final Random random = new Random();

    @RabbitListener(queues = RabbitConfig.REQUEST_QUEUE)
    public void receberMensagem(PagamentoRequest request) {
        System.out.println("==== Banco Tranquilo recebeu: " + request);

        if (random.nextInt(100) < 70) {
            sucessoProdutor.enviar(new PagamentoResponse(
                    request.pedidoId(), "APROVADO", "Pagamento aprovado pelo Banco Tranquilo"));
        } else {
            erroProdutor.enviar(new PagamentoResponse(
                    request.pedidoId(), "RECUSADO", "Pagamento recusado pelo Banco Tranquilo"));
        }
    }
}