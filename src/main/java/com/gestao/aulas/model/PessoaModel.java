package com.gestao.aulas.model;


import com.gestao.aulas.util.Util;
import java.time.LocalDate;
import java.time.LocalDateTime;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "pessoa")

public class PessoaModel {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
  
    @Column(name = "nome")
    private String nome;


    @Column(name = "dtnasci")
    private LocalDate nascimento;

    @Column(name = "telefone1")
    private String telefone1;

    @Column(name = "telefone2")
    private String telefone2;

    @Column(name = "telefone3")
    private String telefone3;

    @Column(name = "email")
    private String email;

    @Column(name = "endereco")
    private String endereco;

    @Column(name = "complemento")
    private String complemento;

    @Column(name = "observacao")
    private String observacao;
    
    @Column(name = "bairro_id")
    private Long bairro_id;

    @Column(name = "numero")
    private String numero;

   /* @Column(name = "pessoa_id")
    private Integer pessoa_id;
*/
    @Column(name = "celular")
    private String celular;
    
    @Column(name = "dt_nascimento")
    private String dtNascimento;
    
    @Column(name = "rg")
    private String rg;
    
    @Column(name = "cpf")
    private String cpf;
    
    @Column(name = "terapeuta")
    private String terapeuta;
    
    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = true)
    private PessoaModel responsavel;

    @Column(name = "dt_cadastro")
    private LocalDateTime dtCadastro;
    
    @Column(name = "dt_ultima_edt")
    private LocalDateTime dtUltimaEdt;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id_edt", nullable = true)
    private UsuarioModel usuarioEdt;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id_cad", nullable = true)
    private UsuarioModel usuarioCad;
    
    public void setDtUltimaEdt(String date) {
    	this.dtUltimaEdt = Util.ConvStrToLocalDateTime(date);
    }
    
    public String getDtUltimaEdt() {
    	return Util.ConvLocalDateTimeToStr(this.dtUltimaEdt);
    }
    
    public void setDtCadastro(String date) {
    	this.dtCadastro = Util.ConvStrToLocalDateTime(date);
    }
    
    public String getDtCadastro() {
    	return Util.ConvLocalDateTimeToStr(this.dtCadastro);
    }
    
    public void setNascimento(String date){
        this.nascimento = Util.ConvStrToLocalDate(date);
    }
    public String getNascimento(){
        return Util.ConvLocalDateToStr(this.nascimento);
    }
    //constructor, setters and getters omitted for brevity
}