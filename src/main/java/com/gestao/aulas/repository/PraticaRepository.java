package com.gestao.aulas.repository;

import com.gestao.aulas.model.PraticaModel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PraticaRepository extends JpaRepository<PraticaModel, Integer> {

    List<PraticaModel>  findByTipoPraticaOrderByNomeAsc(String Tipopratica);
    

}