package br.com.washii.domain.exceptions;

public class SenhaInvalidaException extends NegocioException {

    public SenhaInvalidaException() {
        super("A senha informada está incorreta.");
    }

}
