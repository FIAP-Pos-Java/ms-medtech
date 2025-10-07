package br.com.medtech.ms_medtech.handlers;

import br.com.medtech.ms_medtech.dtos.erro.ErroResponseDTO;
import br.com.medtech.ms_medtech.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LoginCadastradoException.class)
    public ResponseEntity<ErroResponseDTO> handleLoginCadastradoException(final LoginCadastradoException e) {
        ErroResponseDTO exception = new ErroResponseDTO(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception);
    }

    @ExceptionHandler(LoginNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleLoginNaoEncontradoException(final LoginNaoEncontradoException e) {
        ErroResponseDTO exception = new ErroResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ExceptionHandler(UsuarioCadastradoException.class)
    public ResponseEntity<ErroResponseDTO> handleUsuarioCadastradoException(final UsuarioCadastradoException e) {
        ErroResponseDTO exception = new ErroResponseDTO(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ErroResponseDTO> handleUsuarioNaoEnontradoException(final UsuarioNaoEncontradoException e) {
        ErroResponseDTO exception = new ErroResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponseDTO> handleIllegalArgumentException(final IllegalArgumentException e) {
        ErroResponseDTO exception = new ErroResponseDTO(HttpStatus.BAD_REQUEST.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception);
    }

    @ExceptionHandler(ConsultaEncontradaException.class)
    public ResponseEntity<ErroResponseDTO> handlerConsultaEncontradaException(final ConsultaEncontradaException e) {
        ErroResponseDTO exception = new ErroResponseDTO(HttpStatus.CONFLICT.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception);
    }

    @ExceptionHandler(ConsultaNaoEncontadaException.class)
    public ResponseEntity<ErroResponseDTO> handlerConsultaNaoEncontradaException(final ConsultaNaoEncontadaException e) {
        ErroResponseDTO exception = new ErroResponseDTO(HttpStatus.NOT_FOUND.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception);
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<ErroResponseDTO> handlerAcessoNegadoException(final AcessoNegadoException e) {
        ErroResponseDTO exception = new ErroResponseDTO(HttpStatus.FORBIDDEN.value(), e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
