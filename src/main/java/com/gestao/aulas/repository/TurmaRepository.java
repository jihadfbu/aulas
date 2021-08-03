package com.gestao.aulas.repository;

import com.gestao.aulas.model.TurmaModel;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<TurmaModel, Integer> {
    
    List<TurmaModel> findAll(Sort sort);
}