package br.com.medtech.ms_medtech.services;

import br.com.medtech.ms_medtech.config.RabbitMQConfiguration;
import br.com.medtech.ms_medtech.dtos.rabbit.EnviarConsultaDTO;
import br.com.medtech.ms_medtech.entities.Consulta;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultaProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void enviarConsulta(Consulta consulta) {
        EnviarConsultaDTO dto = new EnviarConsultaDTO(
                consulta.getDataConsulta(),
                consulta.getPacienteId(),
                consulta.getMedicoId()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE, RabbitMQConfiguration.ROUTING_KEY, dto);
    }
}
