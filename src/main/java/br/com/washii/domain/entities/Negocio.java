package br.com.washii.domain.entities;

import java.time.LocalTime;
import br.com.washii.domain.enums.TipoUsuario;

public abstract class Negocio extends Usuario {
    private String cnpj;
    private String razaoSocial;
    private LocalTime duracaoMediaServico = LocalTime.of(1, 0);
    private LocalTime inicioExpediente = LocalTime.of(8,0);
    private LocalTime fimExpediente = LocalTime.of(17, 0);
    private int capacidadeAtendimentoSimultaneo = 1;


    public Negocio() {
        super();
    }

    public Negocio(String nome, String email, String senha, Endereco endereco, TipoUsuario tipoUsuario) {
        super(nome, email, senha, endereco, tipoUsuario);
    }

    public Negocio(
        Long id, 
        String nome, 
        String email, 
        String senha, 
        Endereco endereco, 
        TipoUsuario tipoUsuario, 
        String cnpj, 
        String razaoSocial, 
        LocalTime duracaoMediaServico, 
        LocalTime horarioIncio, 
        LocalTime horarioEncerramento, 
        int capacidadeAtendimentoSimultaneo
    ) {
        super(id, nome, email, senha, endereco, tipoUsuario);

        setCnpj(cnpj); 
        this.razaoSocial = razaoSocial;
        this.duracaoMediaServico = duracaoMediaServico;
        this.inicioExpediente = horarioIncio;
        this.fimExpediente = horarioEncerramento;
        this.capacidadeAtendimentoSimultaneo = capacidadeAtendimentoSimultaneo;
    }

    public String getCnpj() {
        return cnpj;
    }

    /**
     * Define o CNPJ removendo caracteres especiais.
     */
    public void setCnpj(String cnpj) {
        // cnpj nulo por enquanto - geraldo
        if (cnpj == null) {
            this.cnpj = null;
            return;
        }
        this.cnpj = cnpj.replaceAll("[^0-9]", "");
    }

    public LocalTime getInicioExpediente() {
        return this.inicioExpediente;
    }

    public LocalTime getFimExpediente() {
        return this.fimExpediente;
    }

    public void setInicioExpediente(LocalTime horarioInicio){
        this.inicioExpediente = horarioInicio;
    }

    public void setFimExpediente(LocalTime horarioEncerramento) {
        this.fimExpediente = horarioEncerramento;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial == null? "" : razaoSocial.trim();
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Negocio)) return false;
        // Como o ID está em Usuario, usamos o equals da classe pai
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        // Baseado no ID definido na classe Usuario
        return super.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Negocio[id=%d, nome='%s', cnpj='%s', razaoSocial='%s']", getId(), getNome(), cnpj, razaoSocial);
    }
}