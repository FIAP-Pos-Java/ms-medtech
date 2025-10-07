package br.com.medtech.ms_medtech.dtos.rabbit;

import br.com.medtech.ms_medtech.entities.Medico;
import br.com.medtech.ms_medtech.entities.Paciente;
import br.com.medtech.ms_medtech.entities.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public record EnviarConsultaDTO(
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dataConsulta,
        Paciente paciente,
        Medico medico
) {
}
