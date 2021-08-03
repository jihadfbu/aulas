package com.gestao.aulas.repository;

import com.gestao.aulas.model.FechamentoDiaModel;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FechamentoDiaRepository extends JpaRepository<FechamentoDiaModel, Integer>{
	FechamentoDiaModel findFirstByDataAndDtCancelamentoIsNull(LocalDate data);
	
	
	public List<FechamentoDiaModel> findAllByOrderByDataDesc();
	
}
