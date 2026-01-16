package br.com.washii.domain.entities;

import br.com.washii.domain.enums.StatusAgendamento;
import br.com.washii.domain.exceptions.DataRetroativaException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade central que representa o vínculo entre Cliente e Lava-Jato.
 * Gerencia o estado do serviço, data, hora e os itens contratados.
 */
public class Agendamento {
    private Long id;
    private Cliente cliente;
    private Negocio negocio;
    private LocalDate data;
    private LocalTime hora;
    private Veiculo veiculo;
    private StatusAgendamento status;
    private List<Servico> servicos;

    // Construtor Padrão (Essencial para persistência e frameworks)
    public Agendamento() {
        this.servicos = new ArrayList<>();
        this.status = StatusAgendamento.AGENDADO; // Status inicial padrão
    }

    // Construtor para novos agendamentos (antes de ir para o banco)
    public Agendamento(Cliente cliente, Negocio negocio, LocalDate data, LocalTime hora, Veiculo veiculo) {
        this(); // chama o construtor padrão;
        this.cliente = cliente;
        this.negocio = negocio;
        setData(data);
        setHora(hora);
        this.hora = hora;
        this.veiculo = veiculo;
    }

    // Construtor Completo
    public Agendamento(Long id, Cliente cliente, Negocio negocio, LocalDate data, LocalTime hora,
                       Veiculo veiculo, StatusAgendamento status, List<Servico> servicos) {
        this.id = id;
        this.cliente = cliente;
        this.negocio = negocio;
        setData(data);
        setHora(hora);
        this.veiculo = veiculo;
        this.status = status;
        this.servicos = servicos != null ? servicos : new ArrayList<>();
    }

    /**
     * Regra de Negócio: Calcula o valor total do agendamento somando os serviços.
     * Pode ser expandido futuramente para aplicar taxas baseadas no tipo do veículo.
     */
    public double calcularValorTotal() {
        return servicos.stream()
                .mapToDouble(Servico::getPrecoBase)
                .sum();
    }

    // --- Getters e Setters ---

    public void addServico(Servico servico) {
        if (servico != null) {
            this.servicos.add(servico);
        }
    }

    public void removeServico(Servico servico) {
        this.servicos.remove(servico);
    }

    // --- Getters e Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Negocio getNegocio() { return negocio; }
    public void setNegocio(Negocio negocio) { this.negocio = negocio; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { 
        if (data == null){
            throw new IllegalArgumentException("A data não pode ser nula");
        }
        if (data.isBefore(LocalDate.now())){
<<<<<<< HEAD
            // throw new DataRetroativaException();
=======
            //throw new DataRetroativaException();
>>>>>>> d65510279cf5e84b0705b058029d5bc8f1c0bd50
        }
        this.data = data; 
    }

    public LocalTime getHora() { return hora; }
    public void setHora(LocalTime hora) {
        if (hora == null){
            throw new IllegalArgumentException("O horario não pode ser nulo");
        }
        if (hora.isBefore(LocalTime.now())){
<<<<<<< HEAD
          //  throw new DataRetroativaException();
=======
            //throw new DataRetroativaException();
>>>>>>> d65510279cf5e84b0705b058029d5bc8f1c0bd50
        }
        this.hora = hora; 
    }

    public Veiculo getVeiculo() { return veiculo; }
    public void setVeiculo(Veiculo veiculo) { this.veiculo = veiculo; }

    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }

    public List<Servico> getServicos() { return servicos; }
    public void setServicos(List<Servico> servicos) { this.servicos = servicos; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agendamento that = (Agendamento) o;

        if (this.id != null && that.id != null) {
            return Objects.equals(id, that.id);
        }

        // Se sem ID, dois agendamentos são iguais se forem no mesmo Negócio, Data e Hora
        return Objects.equals(negocio, that.negocio) &&
                Objects.equals(data, that.data) &&
                Objects.equals(hora, that.hora);
    }

    @Override
    public int hashCode() {
        return id != null ? Objects.hash(id) : Objects.hash(negocio, data, hora);
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", negocio=" + negocio +
                ", data=" + data +
                ", hora=" + hora +
                ", veiculo=" + veiculo +
                ", status=" + status +
                ", servicos=" + servicos +
                '}';
    }
}
