package br.com.washii.domain;

import java.util.Objects;

import br.com.washii.domain.enums.TipoUsuario;

public abstract class Usuario {
    private Long id;
    private String email;
    private String senha;
    private String nome;
    private TipoUsuario tipoUsuario;

    // Construtores (para diferentes situações)
    public Usuario() {}

    public Usuario(String nome, String email, String senha, TipoUsuario tipoUsuario) {
        this.id = null;
        setNome(nome);
        setEmail(email);
        setSenha(senha);
        this.tipoUsuario = tipoUsuario;
    }

    public Usuario(Long id, String nome, String email, String senha, TipoUsuario tipoUsuario) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters e Setters
    public Long getId(){ return this.id; }
    public String getEmail() { return this.email; }
    public String getSenha() { return this.senha; }
    public String getNome() { return this.nome; }
    public TipoUsuario getTipoUsuario() { return this.tipoUsuario; }

    public void setId(Long id){
        this.id = id;
    }
    public void setEmail(String email){
        if (email == null || email.isBlank()){
            throw new  IllegalArgumentException("O email não pode ser vazio.");
        }
        this.email = email.trim();
    }
    public void setSenha(String senha){
        if (senha == null || senha.isBlank()){
            throw new IllegalArgumentException("A senha não pode ser vazia.");
        }
        this.senha = senha.trim();
    }
    
    public void setNome(String nome){
        if (nome == null || nome.isBlank()){
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        }
        this.nome = nome.trim();
    }
    public void setTipoUsuario(TipoUsuario tipo){
        this.tipoUsuario = tipo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(this.id, usuario.id);
    }

    @Override 
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Usuario[id=%d, nome='%s', e-mail='%s', tipo de usuario='%s']", 
                getId(), getNome(), getEmail(), getTipoUsuario().toString());
    }
}