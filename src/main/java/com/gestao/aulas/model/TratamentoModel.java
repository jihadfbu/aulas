package com.gestao.aulas.model;

import com.gestao.aulas.util.Util;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
@Table(name = "tratamentos")

public class TratamentoModel{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data")
    private LocalDate data;
    
    @Column(name = "hora")
    private String hora;

    @Column(name = "status")
    private String status;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "valor")
    private Float valor;

    @Column(name = "abatimento")
    private Float abatimento;

    @Column(name = "sala")
    private String sala;

    @ManyToOne
    @JoinColumn(name = "pratica_id", nullable = false)
    private PraticaModel pratica;

    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = false)
    private PessoaModel paciente;

    @ManyToOne
    @JoinColumn(name = "terapeuta_pessoa_id", nullable = false)
    private PessoaModel terapeuta;

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
    
    public void setData(String date) {
        this.data = Util.ConvStrToLocalDate(date);
    }

    public String getData() {
        return Util.ConvLocalDateToStr(this.data);
    }

    public String getStatusDesc(){
        String aux="";
        switch(this.status){
            case "0":
                aux = "Cancelado";
               break;
            case "1":
                aux = "Agendado";
               break;
            case "2":
                aux = "Finalizado";
               break;
               default:
                   aux = "Cancelado";
                   break;
        }
        return aux;
    }
}
