package com.gestao.aulas.repository;

import com.gestao.aulas.model.MatriculaModel;
import com.gestao.aulas.model.PessoaModel;
import com.gestao.aulas.model.TurmaModel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface MatriculaRepository extends JpaRepository<MatriculaModel, Integer> {

    List<MatriculaModel> findByPacienteOrderByAtivaDescDataInicioDesc(PessoaModel paciente);
    
    List<MatriculaModel> findByPacienteAndAtiva(PessoaModel paciente,String ativa);
    
    List<MatriculaModel> findByTurmaOrderByPacienteNomeAsc(TurmaModel turma);
    
    
}