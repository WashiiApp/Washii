package br.com.washii.domain.exceptions;

public class EmailNaoCadastradoException extends NegocioException{

    public EmailNaoCadastradoException(String email) {
        super("O e-mail " + email + " não está cadastrado no sistema."); 
    }

}
