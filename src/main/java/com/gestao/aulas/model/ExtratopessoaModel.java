package com.gestao.aulas.model;



import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;


@Data
@Entity
public class ExtratopessoaModel {


    @Id
    @Column(name = "id")
    private String id;
    
    @Column(name = "nome")
    private String nome;    
    
    @Column(name = "paciente")
    private String paciente;    
    
  
    @Column(name = "prod")
    private String prod;    
    
    @Column(name = "dataini")
    private String dataini;    
    
    @Column(name = "total")
    private Double total;
    
    @Column(name = "tipo")
    private String tipo;    

    @Column(name = "de")
    private String de;    

    @Column(name = "para")
    private String para;    


    
    @Column(name = "observacao")
    private String observacao;    

  
}
