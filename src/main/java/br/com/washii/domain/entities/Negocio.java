package br.com.washii.domain.entities;

import br.com.washii.domain.enums.TipoUsuario;

public abstract class Negocio extends Usuario {
    private String cnpj;
    private Endereco endereco;
    private String razaoSocial;


    public Negocio() {
        super();
    }

    public Negocio(String nome, String email, String senha, TipoUsuario tipoUsuario) {
        super(nome, email, senha, tipoUsuario);
    }

    public Negocio(Long id, String nome, String email, String senha, TipoUsuario tipoUsuario, 
                   String cnpj, Endereco endereco, String razaoSocial) {
        super(id, nome, email, senha, tipoUsuario);
        setCnpj(cnpj); 
        this.endereco = endereco;
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    /**
     * Define o CNPJ removendo caracteres especiais e validando se não está vazio.
     */
    public void setCnpj(String cnpj) {
        if (cnpj == null || cnpj.trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório para um Negócio.");
        }
        // Remove tudo que não for número (pontos, barras e traços)
        this.cnpj = cnpj.replaceAll("[^0-9]", "");
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
            throw new IllegalArgumentException("Razão Social não pode ser vazia.");
        }
        this.razaoSocial = razaoSocial.trim();
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
        return String.format("Negocio[id=%d, nome='%s', cnpj='%s', razaoSocial='%s']", 
                getId(), getNome(), cnpj, razaoSocial);
    }
}