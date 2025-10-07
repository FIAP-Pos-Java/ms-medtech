package br.com.medtech.ms_medtech.mappers;

import br.com.medtech.ms_medtech.dtos.consulta.AtualizarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.consulta.CadastrarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.consulta.MostrarTodasConsultasDTO;
import br.com.medtech.ms_medtech.entities.Consulta;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsutaMapper {
    Consulta toCadastrarConsulta(CadastrarConsultaDTO cadastrarConsultaDTO);
    Consulta toAtualizarConsulta(AtualizarConsultaDTO atualizarConsultaDTO);
    MostrarTodasConsultasDTO toMostrarTodasConsultasDTO(Consulta consulta);
}
