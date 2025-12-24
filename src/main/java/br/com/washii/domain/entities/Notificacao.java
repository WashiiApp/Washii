package br.com.washii.domain.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Notificacao {
    private Long id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataHora;
    private boolean lida;
    private Usuario destinatario; // Para quem é a notificação

    public Notificacao() {
    }

    public Notificacao(String titulo, String mensagem, LocalDateTime dataHora, boolean lida, Usuario destinatario) {
        this.id = null;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataHora = dataHora;
        this.lida = lida;
        this.destinatario = destinatario;
    }

    public Notificacao(Long id, String mensagem, String titulo, LocalDateTime dataHora, boolean lida, Usuario destinatario) {
        this.id = id;
        this.mensagem = mensagem;
        this.titulo = titulo;
        this.dataHora = dataHora;
        this.lida = lida;
        this.destinatario = destinatario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo.trim();
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem.trim();
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public boolean isLida() {
        return lida;
    }

    public void setLida(boolean lida) {
        this.lida = lida;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(Usuario destinatario) {
        this.destinatario = destinatario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Notificacao notificacao = (Notificacao) o;

        // Se o ID for nulo, eles só são iguais se forem o MESMO objeto (já verificado acima)
        if (this.id == null || notificacao.id == null) {
            return false;
        }

        return Objects.equals(getId(), notificacao.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", mensagem='" + mensagem + '\'' +
                ", dataHora=" + dataHora +
                ", lida=" + lida +
                ", destinatario=" + destinatario +
                '}';
    }
}
