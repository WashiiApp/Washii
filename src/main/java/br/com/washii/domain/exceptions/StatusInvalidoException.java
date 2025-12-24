package br.com.washii.domain.exceptions;

public class StatusInvalidoException extends NegocioException{
    public StatusInvalidoException(){
        super("Impossível mudar de status");
    }

    public StatusInvalidoException (String mensagem) {
        super(mensagem);
    }

    public StatusInvalidoException(String statusAtual, String novoStatus) {
        super("Não é possível alterar o agendamento de '" + statusAtual + "' para '" + novoStatus + "'.");
    }
}
