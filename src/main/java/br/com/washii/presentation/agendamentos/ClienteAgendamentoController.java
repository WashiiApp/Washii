package br.com.washii.presentation.agendamentos;

import br.com.washii.domain.entities.LavaJato;
import br.com.washii.service.AgendamentoService;

public class ClienteAgendamentoController {

    private AgendamentoService agendamentoService;

    private LavaJato lavaJato;

    public ClienteAgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    public void setLavaJato(LavaJato lavaJato){
        this.lavaJato = lavaJato;
    }

}
