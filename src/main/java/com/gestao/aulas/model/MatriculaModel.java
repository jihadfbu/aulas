/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestao.aulas.model;

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

import com.gestao.aulas.util.Util;

import lombok.Data;

@Data
@Entity
@Table(name = "matriculas")
public class MatriculaModel {
    
        @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "dia_vencimento")
    private Integer dia_vencimento;

    @Column(name = "data_fim")
    private LocalDate dataFim;
    
    @Column(name = "bolsa")
    private String bolsa;

    @Column(name = "ativa")
    private String ativa;

    @Column(name = "valor")
    private Float valor;
    
    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = false)
    private TurmaModel turma;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private PessoaModel paciente;
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
    


    public void setDataInicio(String date) {
    	this.dataInicio = Util.ConvStrToLocalDate(date);
    }

    public String getDataInicio() {
    	return Util.ConvLocalDateToStr(this.dataInicio);
    }

    public void setDataFim(String dataf) {
    	this.dataFim = Util.ConvStrToLocalDate(dataf);
    }

    public String getBolsaDescricao(){
        if(this.bolsa.equals("1")){
            return "Sim";
        }
        return "Não";
    }
    
    public String getAtivaDescricao(){
        if(this.ativa.equals("1")){
            return "Sim";
        }
        return "Não";
    }
    
    public String getData_fim() {
    	return Util.ConvLocalDateToStr(this.dataFim);
    }

   
}
