package com.gestao.aulas.repository;

import com.gestao.aulas.model.PessoaModel;
import com.gestao.aulas.model.SaldoModel;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PessoaRepository extends JpaRepository<PessoaModel, Integer> {

    public Object findAllById(Integer idResponsavel);

    List<PessoaModel> findByTerapeutaOrderByNomeAsc(String terapeuta);
    
    List<PessoaModel> findAll(Sort sort);

}