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
@Table(name = "fechamentos_dia")
public class FechamentoDiaModel {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data")
    private LocalDate data;
    

    @Column(name = "dt_fechamento")
    private LocalDateTime dtFechamento;

    @Column(name = "dt_cancelamento")
    private LocalDateTime dtCancelamento;

    @ManyToOne
    @JoinColumn(name = "usuario_id_fechamento", nullable = true)
    private UsuarioModel usuarioFech;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id_cancelamento", nullable = true)
    private UsuarioModel usuarioCanc;

    public String getData() {
        return Util.ConvLocalDateToStr(this.data);
    }
    
    public void setData(String date) {
        this.data = Util.ConvStrToLocalDate(date);
    }
    
    public String getDtFechamento() {
        return Util.ConvLocalDateTimeToStr(this.dtFechamento);
    }
    
    public void setDtFechamento(String date) {
        this.dtFechamento = Util.ConvStrToLocalDateTime(date);
    }

    public String getDtCancelamento() {
        return Util.ConvLocalDateTimeToStr(this.dtCancelamento);
    }
    
    public void setDtCancelamento(String date) {
        this.dtCancelamento = Util.ConvStrToLocalDateTime(date);
    }

    
}
