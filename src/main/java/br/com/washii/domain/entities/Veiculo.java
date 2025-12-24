package br.com.washii.domain.entities;

import br.com.washii.domain.enums.CategoriaVeiculo;

import java.util.Objects;

public class Veiculo {
    private Long id;
    private String placa;
    private CategoriaVeiculo categoriaVeiculo;

    public Veiculo() {
    }

    public Veiculo(CategoriaVeiculo categoriaVeiculo, String placa) {
        this.id = null;
        this.categoriaVeiculo = categoriaVeiculo;
        setPlaca(placa);
    }

    public Veiculo(Long id, String placa, CategoriaVeiculo categoriaVeiculo) {
        this.id = id;
        this.placa = placa;
        this.categoriaVeiculo = categoriaVeiculo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        if(placa == null || placa.isBlank()){
            throw new IllegalArgumentException("A placa não pode ser vazia");
        }
        this.placa = placa.trim();
    }

    public CategoriaVeiculo getCategoriaVeiculo() {
        return categoriaVeiculo;
    }

    public void setCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
        this.categoriaVeiculo = categoriaVeiculo;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;

        // Se o ID for nulo, eles só são iguais se forem o MESMO objeto (já verificado acima)
        if (this.id == null || veiculo.id == null) {
            return false;
        }

        return Objects.equals(getId(), veiculo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Veiculo{" +
                "id=" + id +
                ", placa='" + placa + '\'' +
                ", categoriaVeiculo=" + categoriaVeiculo +
                '}';
    }
}
