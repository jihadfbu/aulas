package com.gestao.aulas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "praticas_produtos")

public class PraticaModel {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
  
    @Column(name = "nome")
    private String nome;

    @Column(name = "percentual")
    private Integer percentual;

    @Column(name = "tipo_cobranca")
    private String tipoCobranca;

    @Column(name = "valor")
    private Float valor;

    @Column(name = "tipo_pratica")
    private String tipoPratica;

    public String getTipoPraticaDesc(){
        String aux="";
        switch(this.tipoPratica){
            case "T":
                aux = "Tratamento";
               break;
            case "M":
                aux = "Matriculável";
               break;
            case "A":
                aux = "Avulsa";
               break;
            case "S":
                aux = "Sub-prática";
               break;
        }
        return aux;
    }
    public String getTipoCobrancaDesc(){
        String aux="";
        switch(this.tipoCobranca){
            case "I":
                aux = "Individual";
               break;
            case "M":
                aux = "Mensal";
               break;
            case "P":
                aux = "Pacote";
               break;
        }
        return aux;
    }
}