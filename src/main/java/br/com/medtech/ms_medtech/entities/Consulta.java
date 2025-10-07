package br.com.medtech.ms_medtech.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_consulta")
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime dataConsulta;
    private String observacao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente pacienteId;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medicoId;
}
