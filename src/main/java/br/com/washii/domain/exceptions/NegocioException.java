package br.com.washii.domain.exceptions;

public class NegocioException extends RuntimeException {
    
    // Você pode passar apenas a mensagem de erro
    public NegocioException(String mensagem) {
        super(mensagem);
    }

    // Ou passar a mensagem e a causa original (se houver)
    public NegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}
