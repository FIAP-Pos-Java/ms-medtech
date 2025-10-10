package br.com.medtech.ms_medtech.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String EXCHANGE_NAME = "consultas.core.exchange";
    public static final String QUEUE_MEDTECH_CORE_AGENDADA = "consulta.medtech.core.agendada.queue";
    public static final String QUEUE_MEDTECH_CORE_CANCELADA = "consulta.medtech.core.cancelada.queue";
    public static final String ROUTING_KEY_MEDTECH_CORE_AGENDADA = "consulta.medtech.core.agendada";
    public static final String ROUTING_KEY_MEDTECH_CORE_CANCELADA = "consulta.medtech.core.cancelada";

    @Bean
    public DirectExchange consultasExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queueMedtechCoreAgendada() {
        return new Queue(QUEUE_MEDTECH_CORE_AGENDADA, true);
    }

    @Bean
    public Queue queueMedtechCoreCancelada() {
        return new Queue(QUEUE_MEDTECH_CORE_CANCELADA, true);
    }

    @Bean
    public Binding bindingMedtechCoreAgendada(Queue queueMedtechCoreAgendada, DirectExchange consultasExchange) {
        return BindingBuilder.bind(queueMedtechCoreAgendada)
                .to(consultasExchange)
                .with(ROUTING_KEY_MEDTECH_CORE_AGENDADA);
    }

    @Bean
    public Binding bindingMedtechCoreCancelada(Queue queueMedtechCoreCancelada, DirectExchange consultasExchange) {
        return BindingBuilder.bind(queueMedtechCoreCancelada)
                .to(consultasExchange)
                .with(ROUTING_KEY_MEDTECH_CORE_CANCELADA);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}
