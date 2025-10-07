package br.com.medtech.ms_medtech.services;

import br.com.medtech.ms_medtech.config.RabbitMQConfiguration;
import br.com.medtech.ms_medtech.dtos.rabbit.EnviarUsuarioDTO;
import br.com.medtech.ms_medtech.entities.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioProducerService {

    private final RabbitTemplate rabbitTemplate;

    public void enviarUsuario(Usuario usuario) {
        EnviarUsuarioDTO dto = new EnviarUsuarioDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getLogin().getEmail(),
                usuario.getRole()
        );

        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE, RabbitMQConfiguration.ROUTING_KEY, dto);
    }
}
