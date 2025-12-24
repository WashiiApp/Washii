package br.com.washii.domain.exceptions;

import java.time.LocalDate;
import java.time.LocalTime;

public class HorarioIndisponivelException extends NegocioException {

    public HorarioIndisponivelException(LocalDate data, LocalTime hora) {
        super("Infelizmente, o horário das " + hora + " no dia " + data + " já está lotado.");
    }

    public HorarioIndisponivelException() {
        super("Infelizmente não temos vagas disponíveis para o horário selecionado.");
    }
}
