package br.com.medtech.ms_medtech.services;

import br.com.medtech.ms_medtech.config.RabbitMQConfiguration;
import br.com.medtech.ms_medtech.dtos.rabbit.EnviarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.rabbit.NotificacaoDTO;
import br.com.medtech.ms_medtech.entities.Consulta;
import br.com.medtech.ms_medtech.enums.StatusDaConsulta;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultaProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void enviarConsulta(Consulta consulta) {
        NotificacaoDTO dto = new NotificacaoDTO(
                consulta.getId(),
                consulta.getPacienteId().getId(),
                consulta.getMedicoId().getId(),
                consulta.getDataConsulta()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, RabbitMQConfiguration.ROUTING_KEY_MEDTECH_CORE_AGENDADA, dto);
    }

    public void cancelarConsulta(Consulta consulta) {
        NotificacaoDTO dto = new NotificacaoDTO(
                consulta.getId(),
                consulta.getPacienteId().getId(),
                consulta.getMedicoId().getId(),
                consulta.getDataConsulta()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, RabbitMQConfiguration.ROUTING_KEY_MEDTECH_CORE_CANCELADA, dto);
    }
}
