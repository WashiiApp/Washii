package br.com.washii.domain.entities;

import br.com.washii.domain.enums.CategoriaVeiculo;
import java.time.LocalTime;
import java.util.List;
import br.com.washii.domain.enums.TipoUsuario;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidade que representa a unidade física do Lava-Jato e suas regras de operação.
 */
public class LavaJato extends Negocio {

    private LocalTime duracaoMediaServico = LocalTime.of(1, 0,0);
    private int capacidadeAtendimentoSimultaneo = 1; // Quantidade de vagas/boxes
    private Set<CategoriaVeiculo> categoriasAceitas = new HashSet<>();
    private List<Servico> servicosOferecidos = new ArrayList<>();

    // Construtor padrão
    public LavaJato() {
        super();
    }

    public LavaJato(String nome, String email, String senha, TipoUsuario tipoUsuario) {
        super(nome, email, senha, tipoUsuario);
    }


    public LavaJato(Long id, String nome, String email, String senha, TipoUsuario tipoUsuario, String cnpj, Endereco endereco, String razaoSocial, LocalTime duracaoMediaServico, int capacidadeAtendimento, Set<CategoriaVeiculo> categoriasAceitas, List<Servico> servicosOferecidos) {
        super(id, nome, email, senha, tipoUsuario, cnpj, endereco, razaoSocial);
        this.duracaoMediaServico = duracaoMediaServico;
        this.capacidadeAtendimentoSimultaneo = capacidadeAtendimento;
        this.categoriasAceitas = categoriasAceitas;
        this.servicosOferecidos = servicosOferecidos;
    }

    /**
     * Regra de Negócio: Verifica se o Lava-Jato tem estrutura para o veículo do cliente.
     */
    public boolean suportaCategoria(CategoriaVeiculo categoria) {
        return categoriasAceitas.contains(categoria);
    }

    // --- Getters e Setters ---

    public LocalTime getDuracaoMediaServico() {
        return duracaoMediaServico;
    }

    public void setDuracaoMediaServico(LocalTime duracaoMediaServico) {
        this.duracaoMediaServico = duracaoMediaServico;
    }

    public int getCapacidadeAtendimentoSimultaneo() {
        return capacidadeAtendimentoSimultaneo;
    }

    public void setCapacidadeAtendimentoSimultaneo(int capacidadeAtendimentoSimultaneo) {
        this.capacidadeAtendimentoSimultaneo = capacidadeAtendimentoSimultaneo;
    }

    public Set<CategoriaVeiculo> getCategoriasAceitas() {
        return categoriasAceitas;
    }

    public void setCategoriasAceitas(Set<CategoriaVeiculo> categoriasAceitas) {
        this.categoriasAceitas = categoriasAceitas;
    }

    public void addCategoriasAceitas(CategoriaVeiculo categoriaVeiculo){
        this.categoriasAceitas.add(categoriaVeiculo);
    }

    public boolean removeCategoriasAceitas(CategoriaVeiculo categoriaVeiculo) {
        return this.categoriasAceitas.remove(categoriaVeiculo);
    }

    public List<Servico> getServicosOferecidos() {
        return servicosOferecidos;
    }

    public void setServicosOferecidos(List<Servico> servicosOferecidos) {
        this.servicosOferecidos = servicosOferecidos;
    }

    public void addServicosOferecidos(Servico servico){
        this.servicosOferecidos.add(servico);
    }

    public boolean removeServicosOferecidos(Servico servico){
        return this.servicosOferecidos.remove(servico);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof LavaJato)) return false;
        // Como o ID está em Usuario, usamos o equals da classe pai
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        // Baseado no ID definido na classe Usuario
        return super.hashCode();
    }
}