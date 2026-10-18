package br.com.fiap.banco_tranquilo_worker.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String REQUEST_EXCHANGE = "pagamento-request-exchange";
    public static final String REQUEST_QUEUE = "pagamento-request-queue";
    public static final String REQUEST_KEY = "pagamento-request-rout-key";

    public static final String SUCESSO_EXCHANGE = "pagamento-response-sucesso-exchange";
    public static final String SUCESSO_QUEUE = "pagamento-response-sucesso-queue";
    public static final String SUCESSO_KEY = "pagamento-response-sucesso-rout-key";

    public static final String ERRO_EXCHANGE = "pagamento-response-erro-exchange";
    public static final String ERRO_QUEUE = "pagamento-response-erro-queue";
    public static final String ERRO_KEY = "pagamento-response-erro-rout-key";

    @Bean
    public DirectExchange requestExchange() {
        return new DirectExchange(REQUEST_EXCHANGE, true, false);
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(REQUEST_QUEUE, true);
    }

    @Bean
    public Binding requestBinding() {
        return BindingBuilder.bind(requestQueue()).to(requestExchange()).with(REQUEST_KEY);
    }

    @Bean
    public DirectExchange sucessoExchange() {
        return new DirectExchange(SUCESSO_EXCHANGE, true, false);
    }

    @Bean
    public Queue sucessoQueue() {
        return new Queue(SUCESSO_QUEUE, true);
    }

    @Bean
    public Binding sucessoBinding() {
        return BindingBuilder.bind(sucessoQueue()).to(sucessoExchange()).with(SUCESSO_KEY);
    }

    @Bean
    public DirectExchange erroExchange() {
        return new DirectExchange(ERRO_EXCHANGE, true, false);
    }

    @Bean
    public Queue erroQueue() {
        return new Queue(ERRO_QUEUE, true);
    }

    @Bean
    public Binding erroBinding() {
        return BindingBuilder.bind(erroQueue()).to(erroExchange()).with(ERRO_KEY);
    }

    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}