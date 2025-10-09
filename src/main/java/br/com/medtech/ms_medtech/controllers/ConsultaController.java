package br.com.medtech.ms_medtech.controllers;

import br.com.medtech.ms_medtech.dtos.consulta.AtualizarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.consulta.CadastrarConsultaDTO;
import br.com.medtech.ms_medtech.dtos.consulta.MostrarTodasConsultasDTO;
import br.com.medtech.ms_medtech.services.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;
    private static final Logger logger = LoggerFactory.getLogger(ConsultaController.class);

    @PostMapping("cadastrarConsultas")
    public ResponseEntity<Void> cadastrarConsulta(
            @RequestBody CadastrarConsultaDTO cadastroConsultaDTO
    ) {
        this.logger.info("POST -> /consultas/cadastrar");
        this.consultaService.cadastrar(cadastroConsultaDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("visualizarConsultas")
    public ResponseEntity<Page<MostrarTodasConsultasDTO>> buscarTodasConsultas(
            @RequestParam int page,
            @RequestParam int size
    ) {
        this.logger.info("GET /consultas");
        var resultado = this.consultaService.buscarTodasConsultas(page, size);
        return new ResponseEntity(resultado.getContent(), HttpStatus.OK);
    }

    @GetMapping("/visualizarConsultasPacientes/{id}")
    public ResponseEntity<Page<MostrarTodasConsultasDTO>> buscarTodasConsultasDoPaciente(
            @PathVariable String id,
            @RequestParam int page,
            @RequestParam int size
    ) {
        this.logger.info("GET /consultas/paciente/"+id);
        var resultado = this.consultaService.buscarTodasConsultasDoPaciente(id, page, size);
        return new ResponseEntity(resultado.getContent(), HttpStatus.OK);
    }

    @PutMapping("editarConsultas/{id}")
    public ResponseEntity<Void> editarConsulta(
            @PathVariable String id,
            @RequestBody AtualizarConsultaDTO atualizarConsultaDTO
    ) {
        this.logger.info("PUT -> /consultas/editar/" + id);
        this.consultaService.editarConsulta(id, atualizarConsultaDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarConsulta(
            @RequestParam String id
    ) {
        this.logger.info("DELETE -> /consultas/deletar/" + id);
        this.consultaService.deletarConsulta(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
