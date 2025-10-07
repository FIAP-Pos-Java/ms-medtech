package br.com.medtech.ms_medtech.services;

import br.com.medtech.ms_medtech.converters.UUIDUtils;

import br.com.medtech.ms_medtech.dtos.medicos.AtualizarMedicoDTO;
import br.com.medtech.ms_medtech.dtos.medicos.CadastrarMedicoDTO;
import br.com.medtech.ms_medtech.dtos.medicos.MostrarMedicoDTO;
import br.com.medtech.ms_medtech.dtos.medicos.MostrarTodosMedicosDTO;
import br.com.medtech.ms_medtech.entities.Medico;
import br.com.medtech.ms_medtech.exceptions.LoginNaoEncontradoException;
import br.com.medtech.ms_medtech.exceptions.UsuarioCadastradoException;
import br.com.medtech.ms_medtech.exceptions.UsuarioNaoEncontradoException;
import br.com.medtech.ms_medtech.mappers.MedicoMapper;
import br.com.medtech.ms_medtech.repositories.LoginRepository;
import br.com.medtech.ms_medtech.repositories.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final LoginRepository loginRepository;
    private final MedicoMapper medicoMapper;
    private final UUIDUtils uuidUtils;

    private final String MESSAGE_MEDICO_ENCONTRADO = "este medico já existe no sistema";
    private final String MESSAGE_MEDICO_NAO_ENCONTRADO = "este medico não está cadastrado";
    private final String MESSAGE_LOGIN_NAO_ENCONTRADO = "este login não existe no sistema, por isso não pode ser feito referencia";

    public void cadastrarMedico(CadastrarMedicoDTO cadastrarMedicoDTO){

        var buscandoLogin = this.loginRepository.findById(cadastrarMedicoDTO.login().getId()).orElseThrow(() -> new LoginNaoEncontradoException(MESSAGE_LOGIN_NAO_ENCONTRADO));
        var buscandoMedico = this.medicoRepository.findByLogin_Id(cadastrarMedicoDTO.login().getId());

        if(buscandoMedico.isPresent()){
            throw new UsuarioCadastradoException(MESSAGE_MEDICO_ENCONTRADO);
        }

        Medico medico = this.medicoMapper.toCadastrarMedico(cadastrarMedicoDTO);
        medico.setDataCadastro(LocalDateTime.now());
        medico.setLogin(buscandoLogin);
        medico.setEnabled(true);
        this.medicoRepository.save(medico);
    }

    public MostrarMedicoDTO buscarMedicoPorId(String idStr){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarMedico = this.medicoRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(MESSAGE_MEDICO_NAO_ENCONTRADO));

        MostrarMedicoDTO mostrarMedicoDTO = this.medicoMapper.toMostrarMedicoDTO(buscarMedico);
        return mostrarMedicoDTO;
    }

    public Page<MostrarTodosMedicosDTO> buscarTodosMedicos(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Medico> medicosPage = this.medicoRepository.findAll(pageable);
        Page<MostrarTodosMedicosDTO> mostrarTodosMedicos = medicosPage.map(this.medicoMapper::toMostrarTodosMedicosDTO);
        return mostrarTodosMedicos;
    }

    public void deletarMedicoPorId(String idStr){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarMedico = this.medicoRepository.findById(id);

        if(buscarMedico.isEmpty()){
            throw new UsuarioNaoEncontradoException(MESSAGE_MEDICO_NAO_ENCONTRADO);
        }

        this.medicoRepository.deleteById(id);
    }

    public void atualizarMedico(String idStr, AtualizarMedicoDTO atualizarMedicoDTO){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarMedico = this.medicoRepository.findById(id);

        if(buscarMedico.isEmpty()){
            throw new UsuarioNaoEncontradoException(MESSAGE_MEDICO_NAO_ENCONTRADO);
        }

        Medico medicoAtualizado = this.medicoMapper.toAtualizarMedico(atualizarMedicoDTO);
        medicoAtualizado.setId(id);
        medicoAtualizado.setDataCadastro(buscarMedico.get().getDataCadastro());
        medicoAtualizado.setLogin(buscarMedico.get().getLogin());
        this.medicoRepository.save(medicoAtualizado);
    }
}
