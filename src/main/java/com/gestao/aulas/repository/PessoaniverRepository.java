package com.gestao.aulas.repository;

import com.gestao.aulas.model.PessoaniverModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PessoaniverRepository extends JpaRepository<PessoaniverModel, Integer> {

    @Query(value = "select id, nome, celular, telefone1, telefone2, telefone3,email, " + 
    		" substring(dt_nascimento,4,2) mes_niver, substring(dt_nascimento,1,5) dt_nascimento from pessoa" + 
    		" order by mes_niver asc, dt_nascimento asc", nativeQuery = true)
    List<PessoaniverModel>  findNiver();
    
}