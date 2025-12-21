package br.com.washii.domain;

import java.util.Objects;

/**
 * Representa o endereço de um Cliente ou Unidade do Lava-Jato.
 */
public class Endereco {
    
    private Long id; // ID para persistência no PostgreSQL
    private String cep;
    private String pais; // Corrigido de 'paiz' para 'pais'
    private String estado;
    private String cidade;
    private String rua;
    private String numero; // Atributo importante que faltava
    private String referencia;

    // 1. CONSTRUTOR VAZIO (Obrigatório para frameworks/JDBC)
    public Endereco() {
    }

    // 2. CONSTRUTOR DE CADASTRO (Sem ID - usado na UI)
    // Pedimos o básico obrigatório para um endereço ser válido
    public Endereco(String cep, String cidade, String rua, String numero, String pais) {
        setCep(cep);
        setCidade(cidade);
        setRua(rua);
        setNumero(numero);
        setPais(pais); 
    }

    // 3. CONSTRUTOR COMPLETO (Usado pelo Felix ao buscar do banco)
    public Endereco(Long id, String cep, String pais, String estado, String cidade, String rua, String numero, String referencia) {
        this.id = id;
        this.cep = cep;
        this.pais = pais;
        this.estado = estado;
        this.cidade = cidade;
        this.rua = rua;
        this.numero = numero;
        this.referencia = referencia;
    }

    // --- GETTERS E SETTERS COM VALIDAÇÕES ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new IllegalArgumentException("CEP não pode ser vazio.");
        }
        this.cep = cep.trim().replace("-", ""); // Remove o traço para padronizar no banco
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = (pais == null) ? "Brasil" : pais.trim();
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        if (cidade == null || cidade.trim().isEmpty()) {
            throw new IllegalArgumentException("Cidade é obrigatória.");
        }
        this.cidade = cidade.trim();
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    // --- MÉTODOS AUXILIARES ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(id, endereco.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return rua + ", " + numero + " - " + cidade + "/" + estado + " (CEP: " + cep + ")";
    }

    /**
     * Verifica se este endereço é o mesmo local físico que outro,
     * 
     */
    public boolean isMesmoLocal(Endereco outro) {
        if (outro == null) return false;
        
    // Comparamos os campos que definem o local físico
    return Objects.equals(this.cep, outro.cep) &&
           Objects.equals(this.numero, outro.numero) &&
           Objects.equals(this.rua.toLowerCase(), outro.rua.toLowerCase());
}
}