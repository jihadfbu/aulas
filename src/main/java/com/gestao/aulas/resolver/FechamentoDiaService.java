package com.gestao.aulas.resolver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.gestao.aulas.model.FechamentoDiaModel;
import com.gestao.aulas.repository.FechamentoDiaRepository;
import com.gestao.aulas.repository.UsuarioRepository;
import com.gestao.aulas.util.Util;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

@Service
@GraphQLApi
public class FechamentoDiaService {

    @Autowired
    private FechamentoDiaRepository fechamento;

    @Autowired
    private UsuarioRepository usuario;
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void novoFechamento(FechamentoDiaModel fecha) {
    	FechamentoDiaModel f = fechamento.findFirstByDataAndDtCancelamentoIsNull(Util.ConvStrToLocalDate(fecha.getData())); 
    	if(f == null) {    		
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			if(fecha.getId()==null || fecha.getId()==0) {
				fecha.setDtFechamento(LocalDateTime.now().format(formatter));
			}
	    	fechamento.save(fecha);
    	}else {
    		throw new RuntimeException("->Data já fechada anteriormente");
    	}
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GraphQLMutation()
    public void cancelaFechamento(Integer id, Integer idUsuario) {
    	FechamentoDiaModel fecha = fechamento.findById(id).get();
    	fecha.setUsuarioCanc(usuario.findById(idUsuario).get());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		fecha.setDtCancelamento(LocalDateTime.now().format(formatter));
    	fechamento.save(fecha);
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<FechamentoDiaModel> listarFechamentos() {
        return fechamento.findAllByOrderByDataDesc();
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public FechamentoDiaModel verFechamentoDia(String data) {
        return fechamento.findFirstByDataAndDtCancelamentoIsNull(Util.ConvStrToLocalDate(data));
    }
    
}
