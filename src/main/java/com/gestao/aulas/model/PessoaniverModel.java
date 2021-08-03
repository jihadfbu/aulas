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
@Table(name = "pessoa")

public class PessoaniverModel {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
  
    @Column(name = "nome")
    private String nome;


    @Column(name = "telefone1")
    private String telefone1;

    @Column(name = "telefone2")
    private String telefone2;

    @Column(name = "telefone3")
    private String telefone3;

    @Column(name = "email")
    private String email;

    @Column(name = "celular")
    private String celular;
    
    @Column(name = "dt_nascimento")
    private String dtNascimento;
    
    @Column(name = "mes_niver")
    private String mesNiver;
    
}