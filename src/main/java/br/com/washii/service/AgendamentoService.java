package br.com.washii.service;

import br.com.washii.domain.entities.Agendamento;
import br.com.washii.domain.entities.Negocio;
import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.domain.exceptions.HorarioIndisponivelException;
import br.com.washii.domain.repository.AgendamentoRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }
    //Solicita um novo agendamento após validar disponibilidade.

    public void solicitarAgendamento(Agendamento agendamento) {
        Objects.requireNonNull(agendamento, "Agendamento não pode ser nulo");

        boolean disponivel = validarDisponibilidade(agendamento.getData(), agendamento.getHora(), agendamento.getNegocio());

        if (!disponivel) {
            throw new HorarioIndisponivelException();
        }

        agendamento.setStatus(StatusAgendamento.AGENDADO);
        repository.salvar(agendamento);
    }
    //Cancela um agendamento existente.

    public void cancelarAgendamento(Agendamento agendamento) {
        Objects.requireNonNull(agendamento, "Agendamento não pode ser nulo");
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        repository.atualizarStatus(agendamento.getId(), StatusAgendamento.CANCELADO);
    }
    //Lista agendamentos por período e negócio.
    public List<Agendamento> listarPorData(LocalDate data, Long negocioId) {
        return repository.listarPorPeriodoENegocio(data, data, negocioId);
    }

    public List<Agendamento> listarAgendamentosDoUsuario(Long userId) {
        Objects.requireNonNull(userId, "ID do usuário não pode ser nulo");
        return repository.listarPorCliente(userId);
    }

    // recebe o objeto negocio que contém horario de inicio, fim duração do serviço;
    private List<LocalTime> gerarHorariosPossiveis(Negocio negocio) {

        List<LocalTime> horarios = new ArrayList<>();
        //cria uma lista vazia , onde serão armazenaods os horarios possieveis;
        LocalTime inicio = negocio.getInicioExpediente();
        LocalTime fim = negocio.getFimExpediente();
        LocalTime duracao = negocio.getDuracaoMediaServico();
        Duration intervalo = Duration.ofHours(duracao.getHour()).plusMinutes(duracao.getMinute());
        //converte para duration que é a classe correta
        LocalTime atual = inicio;
        //variavel para começar o loop

        while (!atual.plus(intervalo).isAfter(fim)) {horarios.add(atual);atual = atual.plus(intervalo);
        }
        return horarios;
        // reotrna a lista de horarios possiveis
        //verifica se o horário + a duração são menores que o fim do expediente;
    }
    public List<LocalTime> listarHorariosDisponiveis(LocalDate data, Negocio negocio) {
        Objects.requireNonNull(data, "Data não pode ser nula");
        Objects.requireNonNull(negocio, "Negócio não pode ser nulo");

        List<LocalTime> horariosPossiveis = gerarHorariosPossiveis(negocio);

        LocalDate hoje = LocalDate.now();
        LocalTime agora = LocalTime.now();

        return horariosPossiveis.stream()
                // se for hoje, só horários depois do horário atual
                .filter(hora -> !data.isEqual(hoje) || hora.isAfter(agora))
                // valida disponibilidade no banco
                .filter(hora -> validarDisponibilidade(data, hora, negocio))
                .toList();
    }

    private boolean validarDisponibilidade(LocalDate data, LocalTime hora, Negocio negocio) {
        int agendamentos = repository.contarAgendamento(negocio.getId(), data, hora);
        return agendamentos < negocio.getCapacidadeAtendimentoSimultaneo();
    }

    //Atualiza o status de um agendamento

    public void atualizarStatus(StatusAgendamento novoStatus, Agendamento agendamento) {
        Objects.requireNonNull(agendamento, "Agendamento não pode ser nulo");
        agendamento.setStatus(novoStatus);
        repository.atualizarStatus(agendamento.getId(), novoStatus);
    }

    public List<Agendamento> listarPorPeriodoENegocio(LocalDate inicio, LocalDate fim, Long negocioId){
        if (inicio == null || fim == null || negocioId == null){
            throw new IllegalArgumentException("Data de inicio e fim e id não podem ser null");
        }

        return repository.listarPorPeriodoENegocio(inicio, fim, negocioId);
    }


}
