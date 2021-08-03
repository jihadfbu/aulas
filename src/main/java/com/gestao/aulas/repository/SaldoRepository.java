package com.gestao.aulas.repository;

import com.gestao.aulas.model.SaldoModel;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SaldoRepository extends JpaRepository<SaldoModel, Integer> {

    @Query(value = "select pes.nome,pes.celular,pes.id, sum(if(es.tipo='C',es.valor,0))-sum(if(es.tipo='D',es.valor,0)) saldo from pessoa pes \n" +
                    "left join entradas_saidas es on pes.id=es.pessoa_id and es.dt_desativacao is null\n" +
                    "group by pes.nome,pes.celular,pes.id order by pes.nome asc", nativeQuery = true)
    List<SaldoModel>  findSaldos();
    
    @Query(value = "select pes.nome,pes.celular,pes.id, sum(if(es.tipo='C',es.valor,0))-sum(if(es.tipo='D',es.valor,0)) saldo from pessoa pes \n" +
                    "left join entradas_saidas es on pes.id=es.pessoa_id and es.dt_desativacao is null\n" +
            "where pes.id=:id \n"+
                    "group by pes.nome,pes.celular,pes.id", nativeQuery = true)
    SaldoModel  findSaldoByPaciente(Integer id);

    
    @Query(value = "select pes.nome,pes.celular,pes.id, sum(if(es.tipo='C',es.valor,0))-sum(if(es.tipo='D',es.valor,0)) saldo from pessoa pes \n" +
            "left join entradas_saidas es on pes.id=es.pessoa_id and es.dt_desativacao is null\n" +
    		" where es.data<= :data"+
            "group by pes.nome,pes.celular,pes.id order by pes.nome asc", nativeQuery = true)
List<SaldoModel>  findSaldosByDataCorte(LocalDate data);

@Query(value = "select pes.nome,pes.celular,pes.id, sum(if(es.tipo='C',es.valor,0))-sum(if(es.tipo='D',es.valor,0)) saldo from pessoa pes \n" +
            "left join entradas_saidas es on pes.id=es.pessoa_id and es.dt_desativacao is null\n" +
    "where pes.id=:id and es.data<= :data\n"+
            "group by pes.nome,pes.celular,pes.id", nativeQuery = true)
SaldoModel  findSaldoByPacienteAndDatacorte(Integer id,LocalDate data);    
    
}