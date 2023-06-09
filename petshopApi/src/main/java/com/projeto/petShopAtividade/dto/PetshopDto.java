package com.projeto.petShopAtividade.dto;

import com.projeto.petShopAtividade.enums.StatusTratamento;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class PetshopDto {

    @Id
    private UUID clienteid;
    @NotBlank
    private String nome;
    @NotBlank
    private String especie;
    @NotBlank
    private String raca;
    @NotBlank
    private String altura;
    @NotBlank
    private String peso;
    @NotBlank
    private String pelagem;
    @NotBlank
    private String tratamento;

    private String statusTratamento;


    private String contato;

    @NotNull
    private double valor;


    private String responsavel;


    public UUID getClienteid() {
        return clienteid;
    }

    public void setClienteid(UUID clienteid) {
        this.clienteid = clienteid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getPelagem() {
        return pelagem;
    }

    public void setPelagem(String pelagem) {
        this.pelagem = pelagem;
    }


    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
    }

    public String getStatusTratamento() {
        return statusTratamento;
    }

    public void setStatusTratamento(String statusTratamento) {
        this.statusTratamento = statusTratamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }
}
