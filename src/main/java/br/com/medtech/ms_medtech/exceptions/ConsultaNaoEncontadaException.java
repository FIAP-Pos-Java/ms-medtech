package br.com.medtech.ms_medtech.exceptions;

public class ConsultaNaoEncontadaException extends RuntimeException {
    public ConsultaNaoEncontadaException(String message) {
        super(message);
    }
}
