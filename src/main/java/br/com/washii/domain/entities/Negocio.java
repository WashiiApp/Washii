package br.com.washii.domain.entities;

import br.com.washii.domain.enums.TipoUsuario;

public abstract class Negocio extends Usuario {
    private String cnpj;
    private String razaoSocial;


    public Negocio() {
        super();
    }

    public Negocio(String nome, String email, String senha, Endereco endereco, TipoUsuario tipoUsuario) {
        super(nome, email, senha, endereco, tipoUsuario);
    }

    public Negocio(Long id, String nome, String email, String senha, Endereco endereco, TipoUsuario tipoUsuario, String cnpj, String razaoSocial) {
        super(id, nome, email, senha, endereco, tipoUsuario);
        setCnpj(cnpj); 
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    /**
     * Define o CNPJ removendo caracteres especiais.
     */
    public void setCnpj(String cnpj) {
        // Remove tudo que não for número (pontos, barras e traços)
        this.cnpj = cnpj.replaceAll("[^0-9]", "");
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
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