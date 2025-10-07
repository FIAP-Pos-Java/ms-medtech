package br.com.medtech.ms_medtech.services;

import br.com.medtech.ms_medtech.converters.UUIDUtils;

import br.com.medtech.ms_medtech.dtos.enfermeiros.AtualizarEnfermeiroDTO;
import br.com.medtech.ms_medtech.dtos.enfermeiros.CadastrarEnfermeiroDTO;
import br.com.medtech.ms_medtech.dtos.enfermeiros.MostrarEnfermeiroDTO;
import br.com.medtech.ms_medtech.dtos.enfermeiros.MostrarTodosEnfermeirosDTO;
import br.com.medtech.ms_medtech.entities.Enfermeiro;
import br.com.medtech.ms_medtech.exceptions.LoginNaoEncontradoException;
import br.com.medtech.ms_medtech.exceptions.UsuarioCadastradoException;
import br.com.medtech.ms_medtech.exceptions.UsuarioNaoEncontradoException;
import br.com.medtech.ms_medtech.mappers.EnfermeiroMapper;
import br.com.medtech.ms_medtech.repositories.EnfermeiroRepository;
import br.com.medtech.ms_medtech.repositories.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EnfermeiroService {
    private final EnfermeiroRepository enfermeiroRepository;
    private final LoginRepository loginRepository;
    private final EnfermeiroMapper enfermeiroMapper;
    private final UUIDUtils uuidUtils;

    private final String MESSAGE_ENFERMEIRO_ENCONTRADO = "este enfermeiro já existe no sistema";
    private final String MESSAGE_ENFERMEIRO_NAO_ENCONTRADO = "este enfermeiro não está cadastrado";
    private final String MESSAGE_LOGIN_NAO_ENCONTRADO = "este login não existe no sistema, por isso não pode ser feito referencia";

    public void cadastrarEnfermeiro(CadastrarEnfermeiroDTO cadastrarEnfermeiroDTO){

        var buscandoLogin = this.loginRepository.findById(cadastrarEnfermeiroDTO.login().getId()).orElseThrow(() -> new LoginNaoEncontradoException(MESSAGE_LOGIN_NAO_ENCONTRADO));
        var buscandoEnfermeiro = this.enfermeiroRepository.findByLogin_Id(cadastrarEnfermeiroDTO.login().getId());

        if(buscandoEnfermeiro.isPresent()){
            throw new UsuarioCadastradoException(MESSAGE_ENFERMEIRO_ENCONTRADO);
        }

        Enfermeiro enfermeiro = this.enfermeiroMapper.toCadastrarEnfermeiro(cadastrarEnfermeiroDTO);
        enfermeiro.setDataCadastro(LocalDateTime.now());
        enfermeiro.setLogin(buscandoLogin);
        enfermeiro.setEnabled(true);
        this.enfermeiroRepository.save(enfermeiro);
    }

    public MostrarEnfermeiroDTO buscarEnfermeiroPorId(String idStr){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarEnfermeiro = this.enfermeiroRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(MESSAGE_ENFERMEIRO_NAO_ENCONTRADO));

        MostrarEnfermeiroDTO mostrarEnfermeiroDTO = this.enfermeiroMapper.toMostrarEnfermeiroDTO(buscarEnfermeiro);
        return mostrarEnfermeiroDTO;
    }

    public Page<MostrarTodosEnfermeirosDTO> buscarTodosEnfermeiros(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Enfermeiro> enfermeirosPage = this.enfermeiroRepository.findAll(pageable);
        Page<MostrarTodosEnfermeirosDTO> mostrarTodosEnfermeiros = enfermeirosPage.map(this.enfermeiroMapper::toMostrarTodosEnfermeirosDTO);
        return mostrarTodosEnfermeiros;
    }

    public void deletarEnfermeiroPorId(String idStr){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarEnfermeiro = this.enfermeiroRepository.findById(id);

        if(buscarEnfermeiro.isEmpty()){
            throw new UsuarioNaoEncontradoException(MESSAGE_ENFERMEIRO_NAO_ENCONTRADO);
        }

        this.enfermeiroRepository.deleteById(id);
    }

    public void atualizarEnfermeiro(String idStr, AtualizarEnfermeiroDTO atualizarEnfermeiroDTO){
        UUID id = this.uuidUtils.retornaStringSemHifen(idStr);
        var buscarEnfermeiro = this.enfermeiroRepository.findById(id);

        if(buscarEnfermeiro.isEmpty()){
            throw new UsuarioNaoEncontradoException(MESSAGE_ENFERMEIRO_NAO_ENCONTRADO);
        }

        Enfermeiro enfermeiroAtualizado = this.enfermeiroMapper.toAtualizarEnfermeiro(atualizarEnfermeiroDTO);
        enfermeiroAtualizado.setId(id);
        enfermeiroAtualizado.setDataCadastro(buscarEnfermeiro.get().getDataCadastro());
        enfermeiroAtualizado.setLogin(buscarEnfermeiro.get().getLogin());
        this.enfermeiroRepository.save(enfermeiroAtualizado);
    }
}