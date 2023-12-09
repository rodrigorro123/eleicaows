package br.com.rodrigo.eleicaows.application.service.amqp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.rodrigo.eleicaows.application.model.response.ResultadoApuracaoResponse;

@Service
public class RabbitMQSender {
	
	private final AmqpTemplate amqpTemplate;

	@Value("${spring.rabbitmq.template.exchange}")
	String exchange;

	@Value("${spring.rabbitmq.template.routingkey}")
	private String routingkey;

	public RabbitMQSender(RabbitTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

	public void sendMessage(ResultadoApuracaoResponse message) {
		amqpTemplate.convertAndSend(exchange, routingkey, message);
	}
}
