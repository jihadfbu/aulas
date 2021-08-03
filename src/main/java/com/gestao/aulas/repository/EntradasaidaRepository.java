package com.gestao.aulas.repository;

import com.gestao.aulas.model.EntradasaidaModel;

import com.gestao.aulas.model.SaldoModel;
import com.gestao.aulas.model.TratamentoModel;
import com.gestao.aulas.model.PessoaModel;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface EntradasaidaRepository extends JpaRepository<EntradasaidaModel, Integer> {

    @Query(value = "select pes.nome,pes.celular,pes.id, sum(if(es.tipo='E',es.valor,0))-sum(if(es.tipo='S',es.valor,0)) saldo from pessoa pes \n" +
                    "left join entradas_saidas es on pes.id=es.pessoa_id\n" +
                    "group by pes.nome,pes.celular,pes.id", nativeQuery = true)
    List<SaldoModel>  findSaldos();
    
    List<EntradasaidaModel> findByPacienteOrderByDataDescIdDesc(PessoaModel paciente);

    List<EntradasaidaModel> findByPacienteIsNotNullAndDataOrderByIdDesc(LocalDate data);
    
    List<EntradasaidaModel> findByTratamentoOrderByIdDesc(TratamentoModel tratamento);

    List<EntradasaidaModel> findByPacienteIsNullOrderByIdDesc();

    List<EntradasaidaModel> findByPacienteIsNullAndDataOrderByDataDescIdDesc(LocalDate data);
    
    List<EntradasaidaModel> findByPacienteIsNullAndDataBetweenOrderByDataDesc(LocalDate data1,LocalDate data2);
    
    List<EntradasaidaModel> findByPacienteIsNotNullAndDataBetweenOrderByDataDescIdDesc(LocalDate data1,LocalDate data2);
    
    List<EntradasaidaModel> findByPacienteAndDataBetweenOrderByDataDescIdDesc(PessoaModel paciente,LocalDate data1,LocalDate data2);
    
    EntradasaidaModel findByEntradaSaida(EntradasaidaModel entradaSaida);
    
    EntradasaidaModel findByPacienteEntradaSaida(EntradasaidaModel entradaSaida);
    
    @Query(value = 
    		"select concat((@rownum \\:=  @rownum +1)) id, T.nome, t.prod, t.total, t.tipo, t.de, t.para  " + 
    		"    		FROM  " + 
    		"    		(SELECT   @rownum \\:=  0 AS rownum,  " + 
    		"    		    		  pe.nome,   " + 
    		"    		if(es.matricula_id is not null, ptu.nome,if(es.tratamento_id is not null,ptr.nome,p.nome)) prod," + 
    		"    		sum(es.valor) total, " + 
    		"    	    es.tipo,pes_d.nome de, pes_p.nome para FROM entradas_saidas es  " + 
    		"    		left join matriculas ma on ma.id=es.matricula_id  " +
    		"    		left join turmas tu on tu.id=ma.turma_id  " +
    		"    		left join praticas_produtos ptu on ptu.id=tu.pratica_id  " + 
    		"    		left join tratamentos tr on tr.id=es.tratamento_id  " + 
    		"            left join entradas_saidas esp on esp.entradasaida_id=es.id" + 
    		"            left join pessoa pes_p on esp.pessoa_id=pes_p.id" + 
    		"            left join entradas_saidas esd on esd.id=es.entradasaida_id" + 
    		"            left join pessoa pes_d on esd.pessoa_id=pes_d.id" + 
    		"            left join praticas_produtos ptr on ptr.id=tr.pratica_id  " + 
    		"    		left join praticas_produtos p on p.id=es.pratica_id  " + 
    		"    		left join pessoa pe on pe.id=es.pessoa_id " + 
    		"    		where es.data between :dataIni and :dataFim  and es.pessoa_id=:idPessoa" + 
    		"    		group by  pe.nome,   if(es.matricula_id is not null, ptu.nome,if(es.tratamento_id is not null,ptr.nome,p.nome)), " + 
    		"    	    es.tipo,pes_d.nome, pes_p.nome) t ", nativeQuery = true)
    List<Object[]> extratoPeriodo(Integer idPessoa, String dataIni, String dataFim);


    @Query(value = 
    		"select concat((@rownum \\:=  @rownum +1)) id, t.prod, t.total, t.tipo, t.dataini " + 
    				"FROM " + 
    				"(SELECT   @rownum \\:=  0 AS rownum, " + 
    				"    		  " +  
    				"if(es.matricula_id is not null, ptu.nome,if(es.tratamento_id is not null,ptr.nome,p.nome)) prod," +
    				"sum(es.valor) total, DATE_SUB(sysdate(), INTERVAL :dias DAY) dataini," +
    				"tipo FROM entradas_saidas es " + 
    	    		"left join matriculas ma on ma.id=es.matricula_id  " +
    				"left join turmas tu on tu.id=ma.turma_id " + 
    				"left join praticas_produtos ptu on ptu.id=tu.pratica_id " + 
    				"left join tratamentos tr on tr.id=es.tratamento_id " + 
    				"left join praticas_produtos ptr on ptr.id=tr.pratica_id " + 
    				"left join praticas_produtos p on p.id=es.pratica_id " + 
    				"where es.data>DATE_SUB(sysdate(), INTERVAL :dias DAY) " + 
    				"group by if(es.matricula_id!=null, ptu.nome,if(es.tratamento_id!=null,ptr.nome,p.nome)), tipo) t ", nativeQuery = true)
    List<Object[]> extratoPeriodoGeral(Integer dias);
}