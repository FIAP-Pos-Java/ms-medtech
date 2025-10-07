package br.com.medtech.ms_medtech.dtos.rabbit;

import br.com.medtech.ms_medtech.entities.Role;

import java.util.UUID;

public record EnviarUsuarioDTO(
        UUID id,
        String nome,
        String email,
        Role role
) {
}
