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
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "entradas_saidas")

public class EntradasaidaModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data")
    private LocalDate data;
      
    @Column(name = "dt_desativacao")
    private LocalDate dt_desativacao;
      
    @Column(name = "tipo")
    private String tipo;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "valor")
    private Float valor;

    @Column(name = "forma_pagamento")
    private String forma_pagamento;

    @ManyToOne
    @JoinColumn(name = "tratamento_id", nullable = true)
    private TratamentoModel tratamento;

    @ManyToOne
    @JoinColumn(name = "matricula_id", nullable = true)
    private MatriculaModel matricula;
/*    
    @ManyToOne
    @JoinColumn(name = "turma_id", nullable = true)
    private TurmaModel turma;
*/
    @ManyToOne
    @JoinColumn(name = "pessoa_id", nullable = true)
    private PessoaModel paciente;
    
    @ManyToOne
    @JoinColumn(name = "pratica_id", nullable = true)
    private PraticaModel pratica;

    @ManyToOne
    @JoinColumn(name = "centrocusto_id", nullable = true)
    private CentroCustoModel centroCusto;
    
    @ManyToOne
    @JoinColumn(name = "entradasaida_id", nullable = true)
    private EntradasaidaModel entradaSaida;    
    
    @ManyToOne
    @JoinColumn(name = "paciente_entradasaida_id", nullable = true)
    private EntradasaidaModel pacienteEntradaSaida;
    //@OneToMany(mappedBy="entradas_saidas")
    @Transient
    private EntradasaidaModel filho;
    
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

    public void setDt_desativacao(String date) {
        this.dt_desativacao = Util.ConvStrToLocalDate(date);
    }

    public String getTipoDescricao() {
        if (this.tipo == null) {
            return null;
        }
        if(this.tipo.equals("C")){
            return "Crédito";
        }
        return "Débito";
        
    }
    
    public String getData() {
        return Util.ConvLocalDateToStr(this.data);
    }
    
    public String getDt_desativacao() {
        return Util.ConvLocalDateToStr(this.dt_desativacao);
    }
    
    public String getDesativado() {
        if (this.dt_desativacao == null) {
            return "Desativado";
        }
        return "";
    }

    
  
}
