package br.com.washii.domain;

import br.com.washii.domain.enums.CategoriaServico;
import java.util.Objects;

/**
 * Representa um serviço oferecido por um Lava-Jato (ex: Lavagem Simples, Polimento).
 * Possui um preço base que pode ser ajustado conforme a categoria do veículo no momento do agendamento.
 */
public class Servico {
    private Long id;
    private String nome;
    private String descricao;
    private CategoriaServico categoriaServico;
    private double precoBase;

    // Construtor Padrão
    public Servico() {
    }

    // Construtor para criação inicial (sem ID)
    public Servico(String nome, String descricao, CategoriaServico categoriaServico, double precoBase) {
        this.nome = nome;
        this.descricao = descricao;
        this.categoriaServico = categoriaServico;
        this.precoBase = precoBase;
    }

    // Construtor Completo para o Felix (Banco de Dados)
    public Servico(Long id, String nome, String descricao, CategoriaServico categoriaServico, double precoBase) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoriaServico = categoriaServico;
        this.precoBase = precoBase;
    }

    // --- Getters e Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public CategoriaServico getCategoriaServico() {
        return categoriaServico;
    }

    public void setCategoriaServico(CategoriaServico categoriaServico) {
        this.categoriaServico = categoriaServico;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public void setPrecoBase(double precoBase) {
        this.precoBase = precoBase;
    }

    // --- Equals e HashCode (Proteção contra IDs nulos) ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Servico servico = (Servico) o;

        // Se ambos têm ID, compara por ID
        if (this.id != null && servico.id != null) {
            return Objects.equals(id, servico.id);
        }

        // Se não têm ID, compara pelos campos de negócio para evitar duplicidade na memória
        return Double.compare(servico.precoBase, precoBase) == 0 &&
                Objects.equals(nome, servico.nome) &&
                categoriaServico == servico.categoriaServico;
    }

    @Override
    public int hashCode() {
        // Se o ID for nulo, gera o hash baseado nos campos de negócio
        return id != null ? Objects.hash(id) : Objects.hash(nome, categoriaServico, precoBase);
    }

    @Override
    public String toString() {
        return "Servico{" +
                "nome='" + nome + '\'' +
                ", precoBase=" + precoBase +
                ", categoria=" + categoriaServico +
                '}';
    }
}