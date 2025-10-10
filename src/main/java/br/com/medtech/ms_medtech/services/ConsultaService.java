package br.com.medtech.ms_medtech.services;

import br.com.medtech.ms_medtech.converters.UUIDUtils;
import br.com.medtech.ms_medtech.dtos.consulta.AtualizarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.consulta.CadastrarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.consulta.MostrarTodasConsultasDTO;
import br.com.medtech.ms_medtech.entities.Consulta;
import br.com.medtech.ms_medtech.enums.StatusDaConsulta;
import br.com.medtech.ms_medtech.exceptions.AcessoNegadoException;
import br.com.medtech.ms_medtech.exceptions.ConsultaEncontradaException;
import br.com.medtech.ms_medtech.exceptions.ConsultaNaoEncontadaException;
import br.com.medtech.ms_medtech.exceptions.UsuarioNaoEncontradoException;
import br.com.medtech.ms_medtech.mappers.ConsultaMapper;
import br.com.medtech.ms_medtech.repositories.ConsultaRepository;
import br.com.medtech.ms_medtech.repositories.MedicoRepository;
import br.com.medtech.ms_medtech.repositories.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaMapper consultaMapper;
    private final ConsultaProducerService consultaProducerService;
    private final UUIDUtils uuidUtils;

    private final String MESSAGE_CONSULTA_ENCONTRADA = "esta consulta já existe";
    private final String MESSAGE_CONSULTA_NAO_ENCONTRADA = "esta consulta não existe";
    private final String MESSAGE_PACIENTE_NAO_ENCONTRADO = "este paciente não está cadastrado";
    private final String MESSAGE_MEDICO_NAO_ENCONTRADO = "este medico não está cadastrado";
    private final String MESSAGE_ACESSO_NEGADO = "Acesso negado";


    public void cadastrar(CadastrarConsultaDTO cadastroConsultaDTO) {
        UUID uuidPaciente = cadastroConsultaDTO.paciente().getId();
        UUID uuidMedico = cadastroConsultaDTO.medico().getId();

        var buscandoPaciente = this.pacienteRepository.findByLogin_Id(uuidPaciente)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(MESSAGE_PACIENTE_NAO_ENCONTRADO));

        var buscandoMedico = this.medicoRepository.findByLogin_Id(uuidMedico)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(MESSAGE_MEDICO_NAO_ENCONTRADO));

        var buscandoConsulta = this.consultaRepository.findByObservacao(cadastroConsultaDTO.observacao());

        if(buscandoConsulta.isPresent()){
            throw new ConsultaEncontradaException(MESSAGE_CONSULTA_ENCONTRADA);
        }

        Consulta consulta = this.consultaMapper.toCadastrarConsulta(cadastroConsultaDTO);
        consulta.setPacienteId(buscandoPaciente);
        consulta.setMedicoId(buscandoMedico);
        this.consultaProducerService.enviarConsulta(consulta);
    }

    public Page<MostrarTodasConsultasDTO> buscarTodasConsultas(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Consulta> consultaPage = this.consultaRepository.findAll(pageable);
        Page<MostrarTodasConsultasDTO> mostrarTodasConsultas = consultaPage.map(this.consultaMapper::toMostrarTodasConsultasDTO);
        return mostrarTodasConsultas;
    }

    public Page<MostrarTodasConsultasDTO> buscarTodasConsultasDoPaciente(String idStr, int page, int size){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User useer = (User) auth.getPrincipal();
        String emailLogado = useer.getUsername();

        var pacienteLogado = this.pacienteRepository.findByLoginEmail(emailLogado);

        if(pacienteLogado.isPresent()){
            if(!pacienteLogado.get().getId().equals(id)){
                throw new AcessoNegadoException(MESSAGE_ACESSO_NEGADO);
            }
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Consulta> consultaPage = this.consultaRepository.findByPacienteId_Id(id, pageable);

        if(consultaPage.isEmpty()){
            throw new ConsultaNaoEncontadaException(MESSAGE_CONSULTA_NAO_ENCONTRADA);
        }

        Page<MostrarTodasConsultasDTO> buscarTodasConsultasDoPaciente = consultaPage.map(this.consultaMapper::toMostrarTodasConsultasDTO);
        return buscarTodasConsultasDoPaciente;
    }

    public void editarConsulta(String idStr, AtualizarConsultaDTO atualizarConsultaDTO) {
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarConsulta = this.consultaRepository.findById(id);

        if(buscarConsulta.isEmpty()){
            throw new ConsultaNaoEncontadaException(MESSAGE_CONSULTA_NAO_ENCONTRADA);
        }

        Consulta consultaAtualizada = this.consultaMapper.toAtualizarConsulta(atualizarConsultaDTO);
        consultaAtualizada.setId(id);
        consultaAtualizada.setPacienteId(buscarConsulta.get().getPacienteId());
        consultaAtualizada.setMedicoId(buscarConsulta.get().getMedicoId());
        consultaAtualizada.setCriadoEm(buscarConsulta.get().getCriadoEm());
        consultaAtualizada.setAtualizadoEm(LocalDateTime.now());
        this.consultaProducerService.enviarConsulta(consultaAtualizada);
        this.consultaRepository.save(consultaAtualizada);
    }

    public void deletarConsulta(String idStr){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarConsulta = this.consultaRepository.findById(id);

        if(buscarConsulta.isEmpty()){
            throw new ConsultaNaoEncontadaException(MESSAGE_CONSULTA_NAO_ENCONTRADA);
        }

        this.consultaRepository.deleteById(id);

        this.consultaProducerService.cancelarConsulta(buscarConsulta.get());
    }
}
