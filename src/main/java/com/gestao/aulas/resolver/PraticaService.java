package com.gestao.aulas.resolver;


import com.gestao.aulas.model.PraticaModel;
import com.gestao.aulas.repository.PraticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
@Service
@GraphQLApi
public class PraticaService{

    @Autowired
    private PraticaRepository pratica;

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void salvarPratica(Integer id, String nome,String tipoPratica,String tipoCobranca,Float valor,Integer percentual
    ) throws ParseException{
        
        PraticaModel praticaM;
        if(id == null || id == 0){
            praticaM = new PraticaModel();
       }else{
            praticaM = (pratica.findById(id)).get();
       }
        praticaM.setNome(nome);
        praticaM.setTipoCobranca(tipoCobranca);
        praticaM.setTipoPratica(tipoPratica);
        praticaM.setValor(valor);
        praticaM.setPercentual(percentual);
        pratica.save(praticaM);
    } 

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<PraticaModel> listarPraticas(){
        return pratica.findAll();
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<PraticaModel> listarPraticasTratamentos(){
        return pratica.findByTipoPraticaOrderByNomeAsc("T");
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<PraticaModel> listarPraticasAvulsos(){
        return pratica.findByTipoPraticaOrderByNomeAsc("A");
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<PraticaModel> listarSubPraticas(){
        return pratica.findByTipoPraticaOrderByNomeAsc("S");
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<PraticaModel> listarPraticasMatriculavel(){
        return pratica.findByTipoPraticaOrderByNomeAsc("M");
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void excluirPratica(Integer id){
        pratica.deleteById(id);
    }

    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public Optional<PraticaModel> retornaPratica(Integer id){
        return pratica.findById(id);
    }
}