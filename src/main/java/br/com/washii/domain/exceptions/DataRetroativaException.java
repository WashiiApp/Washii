package br.com.washii.domain.exceptions;

public class DataRetroativaException extends NegocioException {
    public DataRetroativaException(){
        super("Não é possível realizar agendamentos para datas passadas.");
    }

    public DataRetroativaException(String mensagem){
        super(mensagem);
    }
}
