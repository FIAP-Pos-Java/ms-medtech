package br.com.medtech.ms_medtech.repositories;

import br.com.medtech.ms_medtech.entities.Consulta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {
    Page<Consulta> findByPacienteId_Id(UUID pacienteId, Pageable pageable);
    Optional<Consulta> findByObservacao(String observacao);
}
