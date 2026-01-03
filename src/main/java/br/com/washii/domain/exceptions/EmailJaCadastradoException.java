package br.com.washii.domain.exceptions;

public class EmailJaCadastradoException extends NegocioException {
    public EmailJaCadastradoException() {
        super("Email já cadastrado, tente com outro!");
    }

}
