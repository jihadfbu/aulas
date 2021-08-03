package com.gestao.aulas.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.gestao.aulas.util.Util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;


@Data
@Entity
@Table(name = "turmas")

public class TurmaModel {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "horario")
    private String horario;

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
    
    @Column(name = "data_inicio")
    private LocalDate data_inicio;
    
    @Column(name = "data_fim")
    private LocalDate data_fim;
    
    @Column(name = "ativa")
    private String ativa;
    
    @Column(name = "dias")
    private String dias;

    @ManyToOne
    @JoinColumn(name = "instrutor_pessoa_id", nullable = true)
    private PessoaModel instrutor;

    @ManyToOne
    @JoinColumn(name = "pratica_id", nullable = true)
    private PraticaModel pratica;
    
    public String getAtivaDesc() {
        if (this.ativa.equalsIgnoreCase("1")) {
            return "Ativo";
        }
        return "Inativo";
        
    }

    
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
    
    
    public void setData_inicio(String date){
        if(date==null || date.equals("")){
            this.data_inicio=null;
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);  
            this.data_inicio=localDate;
        }
    }
    
    public String getData_inicio(){
        if(this.data_inicio==null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.data_inicio.format(formatter);
    }
    
    public void setData_fim(String date){
        if(date==null || date.equals("")){
            this.data_fim=null;
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);  
            this.data_fim=localDate;
        }
    }
    
    public String getData_fim(){
        if(this.data_fim==null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.data_fim.format(formatter);
    }
}