/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestao.aulas.resolver;


import com.gestao.aulas.model.PessoaModel;
import com.gestao.aulas.model.PraticaModel;
import com.gestao.aulas.model.TurmaModel;
import com.gestao.aulas.repository.PessoaRepository;
import com.gestao.aulas.repository.PraticaRepository;
import com.gestao.aulas.repository.TurmaRepository;
import com.gestao.aulas.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;

@Service
@GraphQLApi

/**
 *
 * @author ramonsouza
 */
public class TurmaService {
     @Autowired
    private TurmaRepository turma;
    @Autowired
    private PessoaRepository pessoa;

    @Autowired
    private PraticaRepository pratica;
    
    @Autowired
    private UsuarioRepository usuario;

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void salvarTurma(String horario, Integer pratica_id, String data_fim, String data_inicio, String dias, String ativa, Integer instrutor_id, Integer id, Integer idUsuario) throws ParseException {
        TurmaModel tu;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        if (id == null || id == 0) {
            tu = new TurmaModel();
            tu.setDtCadastro(LocalDateTime.now().format(formatter));
            tu.setUsuarioCad(usuario.findById(idUsuario).get());
        } else {
            tu = (turma.findById(id)).get();
            tu.setDtUltimaEdt(LocalDateTime.now().format(formatter));
            tu.setUsuarioEdt(usuario.findById(idUsuario).get());
        }
        tu.setHorario(horario);
        tu.setData_fim(data_fim);
        tu.setData_inicio(data_inicio);
        tu.setDias(dias);
        PessoaModel resp;
        resp = (pessoa.findById(instrutor_id)).get();
        tu.setInstrutor(resp);
            
        PraticaModel prat;
        prat = (pratica.findById(pratica_id)).get();
        tu.setPratica(prat);
        tu.setAtiva(ativa);
        turma.save(tu);
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<TurmaModel> listarTurmas() {
        return turma.findAll(Sort.by("pratica.nome").ascending());
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void excluirTurma(Integer id){
    	turma.deleteById(id);
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public Optional<TurmaModel> retornaTurma(Integer id) {
        return turma.findById(id);
    }
   
}
