package br.com.fiap.banco_tranquilo_worker;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class BancoTranquiloWorkerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancoTranquiloWorkerApplication.class, args);
	}

}
