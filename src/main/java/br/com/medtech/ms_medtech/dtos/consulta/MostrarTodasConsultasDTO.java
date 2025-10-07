package br.com.medtech.ms_medtech.dtos.consulta;

import br.com.medtech.ms_medtech.entities.Medico;
import br.com.medtech.ms_medtech.entities.Paciente;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record MostrarTodasConsultasDTO(
        UUID id,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataConsulta,
        String observacao,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime criadoEm,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime atualizadoEm,
        Paciente paciente,
        Medico medico
) {
}
