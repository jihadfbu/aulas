package com.gestao.aulas.resolver;

import com.gestao.aulas.model.MatriculaModel;
import com.gestao.aulas.model.PessoaModel;
import com.gestao.aulas.model.TurmaModel;
import com.gestao.aulas.repository.MatriculaRepository;
import com.gestao.aulas.repository.UsuarioRepository;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class MatriculaService {

    @Autowired
    private MatriculaRepository matricula;

    @Autowired
    private UsuarioRepository usuario;
   
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void salvarMatricula(MatriculaModel mat, Integer idUsuario) throws ParseException,ConstraintViolationException,DataIntegrityViolationException {
    	try {
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	    	if(mat.getId()==null || mat.getId()==0) {
	    		
	    		mat.setDtCadastro(LocalDateTime.now().format(formatter));
	            mat.setUsuarioCad(usuario.findById(idUsuario).get());
	    	}else {
	    		MatriculaModel matAux = matricula.findById(mat.getId()).get();
	    		mat.setDtCadastro(matAux.getDtCadastro());
	    		mat.setUsuarioCad(matAux.getUsuarioCad());
	    		mat.setDtUltimaEdt(LocalDateTime.now().format(formatter));
	    		mat.setUsuarioEdt(usuario.findById(idUsuario).get());
	    		
	    	}
    		matricula.save(mat);			
		} catch (ConstraintViolationException e) {
			throw new ConstraintViolationException("->Aluno já matriculado anteriormente",null,null);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityViolationException("->Aluno já matriculado anteriormente");
		}
    }

    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<MatriculaModel> listarMatriculas() {
        return matricula.findAll();
    }


    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void excluirMatricula(Integer id){
    	matricula.deleteById(id);
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public Optional<MatriculaModel> retornaMatricula(Integer id) {
        return matricula.findById(id);
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<MatriculaModel> listarMatriculasByPaciente(PessoaModel paciente) {
        return matricula.findByPacienteOrderByAtivaDescDataInicioDesc(paciente);
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<MatriculaModel> listarMatriculasByPacienteAtivo(PessoaModel paciente) {
        return matricula.findByPacienteAndAtiva(paciente, "1");
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<MatriculaModel> listarAlunosByTurma(TurmaModel turma) {
    	return matricula.findByTurmaOrderByPacienteNomeAsc(turma);
    }
}
