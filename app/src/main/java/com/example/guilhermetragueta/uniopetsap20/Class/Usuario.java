package com.example.guilhermetragueta.uniopetsap20.Class;

import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class Usuario {

    // Atributos da classe
    private String id;
    private String nome;
    private String sobrenome;
    private String apelido;
    private String idAcesso;
    private String emailAcesso;


    // Metodos Construtores
    public Usuario() {
    }

    public Usuario(String id, String nome, String sobrenome, String apelido, String idAcesso, String emailAcesso) {
        setId(id);
        setNome(nome);
        setSobrenome(sobrenome);
        setApelido(apelido);
        setidAcesso(idAcesso);
        setEmailAcesso(emailAcesso);
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

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
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

    // Metodo da classe object
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", sobrenome='" + sobrenome + '\'' +
                ", apelido='" + apelido + '\'' +
                ", idAcesso='" + idAcesso + '\'' +
                ", emailAcesso='" + emailAcesso + '\'' +
                '}';
    }
}
