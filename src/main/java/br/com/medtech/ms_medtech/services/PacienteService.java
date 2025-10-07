package br.com.medtech.ms_medtech.services;

import br.com.medtech.ms_medtech.converters.UUIDUtils;
import br.com.medtech.ms_medtech.dtos.pacientes.AtualizarPacienteDTO;
import br.com.medtech.ms_medtech.dtos.pacientes.CadastrarPacienteDTO;
import br.com.medtech.ms_medtech.dtos.pacientes.MostrarPacienteDTO;
import br.com.medtech.ms_medtech.dtos.pacientes.MostrarTodosPacientesDTO;
import br.com.medtech.ms_medtech.entities.Paciente;
import br.com.medtech.ms_medtech.exceptions.LoginNaoEncontradoException;
import br.com.medtech.ms_medtech.exceptions.UsuarioCadastradoException;
import br.com.medtech.ms_medtech.exceptions.UsuarioNaoEncontradoException;
import br.com.medtech.ms_medtech.mappers.PacienteMapper;
import br.com.medtech.ms_medtech.repositories.LoginRepository;
import br.com.medtech.ms_medtech.repositories.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final LoginRepository loginRepository;
    private final PacienteMapper pacienteMapper;
    private final UUIDUtils uuidUtils;

    private final String MESSAGE_PACIENTE_ENCONTRADO = "este paciente já existe no sistema";
    private final String MESSAGE_PACIENTE_NAO_ENCONTRADO = "este paciente não está cadastrado";
    private final String MESSAGE_LOGIN_NAO_ENCONTRADO = "este login não existe no sistema, por isso não pode ser feito referencia";

    public void cadastrarPaciente(CadastrarPacienteDTO cadastrarPacienteDTO){

        var buscandoLogin = this.loginRepository.findById(cadastrarPacienteDTO.login().getId()).orElseThrow(() -> new LoginNaoEncontradoException(MESSAGE_LOGIN_NAO_ENCONTRADO));
        var buscandoPaciente = this.pacienteRepository.findByLogin_Id(cadastrarPacienteDTO.login().getId());

        if(buscandoPaciente.isPresent()){
            throw new UsuarioCadastradoException(MESSAGE_PACIENTE_ENCONTRADO);
        }

        Paciente paciente = this.pacienteMapper.toCadastrarPaciente(cadastrarPacienteDTO);
        paciente.setDataCadastro(LocalDateTime.now());
        paciente.setLogin(buscandoLogin);
        paciente.setEnabled(true);
        this.pacienteRepository.save(paciente);
    }

    public MostrarPacienteDTO buscarPacientePorId(String idStr){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarPaciente = this.pacienteRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(MESSAGE_PACIENTE_NAO_ENCONTRADO));

        MostrarPacienteDTO mostrarPacienteDTO = this.pacienteMapper.toMostrarPacienteDTO(buscarPaciente);
        return mostrarPacienteDTO;
    }

    public Page<MostrarTodosPacientesDTO> buscarTodosPacientes(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Paciente> pacientesPage = this.pacienteRepository.findAll(pageable);
        Page<MostrarTodosPacientesDTO> mostrarTodosPacientes = pacientesPage.map(this.pacienteMapper::toMostrarTodosPacientesDTO);
        return mostrarTodosPacientes;
    }

    public void deletarPacientePorId(String idStr){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarPaciente = this.pacienteRepository.findById(id);

        if(buscarPaciente.isEmpty()){
            throw new UsuarioNaoEncontradoException(MESSAGE_PACIENTE_NAO_ENCONTRADO);
        }

        this.pacienteRepository.deleteById(id);
    }

    public void atualizarPaciente(String idStr, AtualizarPacienteDTO atualizarPacienteDTO){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarPaciente = this.pacienteRepository.findById(id);

        if(buscarPaciente.isEmpty()){
            throw new UsuarioNaoEncontradoException(MESSAGE_PACIENTE_NAO_ENCONTRADO);
        }

        Paciente pacienteAtualizado = this.pacienteMapper.toAtualizarPaciente(atualizarPacienteDTO);
        pacienteAtualizado.setId(id);
        pacienteAtualizado.setDataCadastro(buscarPaciente.get().getDataCadastro());
        pacienteAtualizado.setLogin(buscarPaciente.get().getLogin());
        this.pacienteRepository.save(pacienteAtualizado);
    }
}