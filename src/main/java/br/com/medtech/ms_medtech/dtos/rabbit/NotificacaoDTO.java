package br.com.medtech.ms_medtech.dtos.rabbit;

import br.com.medtech.ms_medtech.enums.StatusDaConsulta;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificacaoDTO(
        UUID pacienteId,
        UUID medicoId,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataConsulta,
        StatusDaConsulta status
) { }
