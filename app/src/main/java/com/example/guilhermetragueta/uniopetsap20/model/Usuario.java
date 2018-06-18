package com.example.guilhermetragueta.uniopetsap20.model;

import java.util.HashMap;
import java.util.Map;

public class Usuario {

    // Atributos da classe
    private String id;
    private String nome;
    private String sobrenome;
    private String idAcesso;
    private String emailAcesso;
    private String imagemPerfil;

    // Metodos Construtores
    public Usuario() {
    }

    public Usuario(String id, String nome, String sobrenome, String idAcesso, String emailAcesso, String imagemPerfil) {
        setId(id);
        setNome(nome);
        setSobrenome(sobrenome);
        setidAcesso(idAcesso);
        setEmailAcesso(emailAcesso);
        setImagemPerfil(imagemPerfil);
    }

    public Usuario(String idAcesso, String emailAcesso) {
        setidAcesso(idAcesso);
        setEmailAcesso(emailAcesso);
    }

    // Metodos de acesso
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getidAcesso() {
        return idAcesso;
    }

    public void setidAcesso(String idAcesso) {
        this.idAcesso = idAcesso;
    }

    public String getEmailAcesso() {
        return emailAcesso;
    }

    public void setEmailAcesso(String emailAcesso) {
        this.emailAcesso = emailAcesso;
    }

    public String getImagemPerfil() {
        return imagemPerfil;
    }

    public void setImagemPerfil(String imagemPerfil) {
        this.imagemPerfil = imagemPerfil;
    }

    // Metodo da classe object
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", idAcesso='" + idAcesso + '\'' +
                ", emailAcesso='" + emailAcesso + '\'' +
                ", imagemPerfil='" + imagemPerfil + '\'' +
                '}';
    }
}
