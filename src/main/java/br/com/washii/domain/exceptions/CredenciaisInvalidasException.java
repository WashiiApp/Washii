package br.com.washii.domain.exceptions;

public class CredenciaisInvalidasException extends NegocioException{
    public CredenciaisInvalidasException(){
        super("Credenciais Inválidas");
    }

    public CredenciaisInvalidasException(String mensagem){
        super(mensagem);
    }
}
