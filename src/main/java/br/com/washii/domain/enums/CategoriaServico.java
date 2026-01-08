package br.com.washii.domain.enums;

public enum CategoriaServico {
    LAVAGEM_SIMPLES("Lavagem Simples"),
    POLIMENTO("Polimento"),
    HIGIENIZACAO("Higienizacao"),
    VETRIFICACAO("Vetrificacao"),
    LAVAGEM_DETALHADA("Lavagem Detalhada"),
    OUTROS("Outros tipos de serviços");

    private final String nome;

    CategoriaServico(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }
}
