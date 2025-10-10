package br.com.medtech.ms_medtech.dtos.rabbit;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificacaoDTO(
        UUID id,
        UUID pacienteId,
        UUID medicoId,
        LocalDateTime dataConsulta
) { }
