package br.com.medtech.ms_medtech.mappers;

import br.com.medtech.ms_medtech.dtos.consulta.AtualizarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.consulta.CadastrarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.consulta.MostrarTodasConsultasDTO;
import br.com.medtech.ms_medtech.entities.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConsultaMapper {


    Consulta toCadastrarConsulta(CadastrarConsultaDTO cadastrarConsultaDTO);
    Consulta toAtualizarConsulta(AtualizarConsultaDTO atualizarConsultaDTO);

    @Mapping(target = "paciente", source = "pacienteId")
    @Mapping(target = "medico", source = "medicoId")
    MostrarTodasConsultasDTO toMostrarTodasConsultasDTO(Consulta consulta);
}
