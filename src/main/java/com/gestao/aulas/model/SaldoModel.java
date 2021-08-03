package com.gestao.aulas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class SaldoModel {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome")
    private String nome;
    
    @Column(name = "celular")
    private String celular;
      
    @Column(name = "saldo")
    private Float saldo;

  
}
