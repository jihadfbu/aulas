package com.gestao.aulas.repository;

import com.gestao.aulas.model.TratamentoModel;
import com.gestao.aulas.model.PessoaModel;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



public interface TratamentoRepository extends JpaRepository<TratamentoModel, Integer> {
    
   
    List<TratamentoModel> findByDataOrderByHoraDesc(LocalDate data);
    List<TratamentoModel> findByPacienteOrderByDataDesc(PessoaModel paciente);
}