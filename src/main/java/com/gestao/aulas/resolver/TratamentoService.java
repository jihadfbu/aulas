package com.gestao.aulas.resolver;

import com.gestao.aulas.model.EntradasaidaModel;
import com.gestao.aulas.model.FechamentoDiaModel;
import com.gestao.aulas.model.PessoaModel;
import com.gestao.aulas.model.PraticaModel;
import com.gestao.aulas.model.TratamentoModel;
import com.gestao.aulas.repository.EntradasaidaRepository;
import com.gestao.aulas.repository.FechamentoDiaRepository;
import com.gestao.aulas.repository.PessoaRepository;
import com.gestao.aulas.repository.PraticaRepository;
import com.gestao.aulas.repository.TratamentoRepository;
import com.gestao.aulas.repository.UsuarioRepository;
import com.gestao.aulas.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.security.access.prepost.PreAuthorize;

@Service
@GraphQLApi
public class TratamentoService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TratamentoRepository tratamento;
    @Autowired
    private EntradasaidaRepository entradasaida;

    @Autowired
    private PessoaRepository pessoa;

    @Autowired
    private PraticaRepository pratica;

    @Autowired
    private FechamentoDiaRepository fr;
    
    @Autowired
    private UsuarioRepository usuario;

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void salvarTratamento(TratamentoModel tra, Integer idUsuario) throws ParseException,RuntimeException, InvalidAttributeValueException {
    	Integer id = tra.getId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        if(tra.getStatus().equals("2")) {
        	FechamentoDiaModel f = fr.findFirstByDataAndDtCancelamentoIsNull(Util.ConvStrToLocalDate(tra.getData()));
        	if(f != null) {
        		throw new InvalidAttributeValueException("->Tratamento não pode ser finalizado, caixa já fechado nesta data!");
        	}        	
        }
    	if(id>0) {
	    	TratamentoModel traDB = tratamento.findById(id).get();
	    	if(traDB.getStatus().equals("2")) {
	    		throw new RuntimeException("->Tratamento já finalizado");
	    	}else {
	    		
	    		tra.setDtCadastro(traDB.getDtCadastro());
	    		tra.setUsuarioCad(traDB.getUsuarioCad());
	    		tra.setUsuarioCad(usuario.findById(idUsuario).get());
	    		
	    		tra.setDtUltimaEdt(LocalDateTime.now().format(formatter));
	    		tra.setUsuarioEdt(usuario.findById(idUsuario).get());
	    		tratamento.save(tra);
	    	}
    	}else {
    		tra.setDtCadastro(LocalDateTime.now().format(formatter));
    		tra.setUsuarioCad(usuario.findById(idUsuario).get());
    		tra = tratamento.save(tra);
    	}
    	if(tra.getStatus().equals("2")) {
			EntradasaidaModel esPac = new EntradasaidaModel();
			esPac.setData(tra.getData());
			esPac.setPaciente(tra.getPaciente());
			esPac.setTipo("D");
			esPac.setTratamento(tra);
			esPac.setValor(tra.getValor());
			entradasaida.save(esPac);    
			PraticaModel prat = pratica.findById(tra.getPratica().getId()).get();
			if(prat.getPercentual()>0) {
    			EntradasaidaModel esTer = new EntradasaidaModel();
    			esTer.setData(tra.getData());
    			esTer.setPaciente(tra.getTerapeuta());
    			esTer.setTipo("C");
    			esTer.setTratamento(tra);
    			esTer.setValor(tra.getValor()*prat.getPercentual()/100);
    			entradasaida.save(esTer);
			}
		}
    	
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<TratamentoModel> listarTratamentos(Integer idPaciente,Integer idTerapeuta,String data) {
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TratamentoModel> q = cb.createQuery(TratamentoModel.class);
        Root<TratamentoModel> from = q.from(TratamentoModel.class);
    	q.select(from);
    	
    	Predicate predicate = cb.and();
    	if(!data.isEmpty() && data!=null) {
    		LocalDate Ndata = Util.ConvStrToLocalDate(data);
    		predicate = cb.and(predicate, cb.equal(from.get("data"), Ndata));
    	}
    	if (idPaciente != null && idPaciente > 0){
    	     predicate = cb.and(predicate, cb.equal(from.get("paciente"), pessoa.findById(idPaciente).get()));
    	}
    	if (idTerapeuta != null && idTerapeuta > 0){
	   	     predicate = cb.and(predicate, cb.equal(from.get("terapeuta"), pessoa.findById(idTerapeuta).get()));
	   	}
    	TypedQuery<TratamentoModel> typedQuery = entityManager.createQuery(
    	    q.select(from )
    	    .where( predicate )
    	    .orderBy(cb.desc(from.get("data")))
    	);
    	return typedQuery.getResultList();    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<TratamentoModel> listarTratamentoByPessoa(PessoaModel pessoa) {
        return tratamento.findByPacienteOrderByDataDesc(pessoa);
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<TratamentoModel> listarAtendimentosHoje() {
        LocalDate hoje = LocalDate.now();
        return tratamento.findByDataOrderByHoraDesc(hoje);
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void excluirTratamento(Integer id){
        tratamento.deleteById(id);
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void revogarTratamento(Integer id){
        TratamentoModel tra = tratamento.findById(id).get();
        
        List<EntradasaidaModel> es = entradasaida.findByTratamentoOrderByIdDesc(tra);
        for (EntradasaidaModel entsai : es) {
            entradasaida.deleteById(entsai.getId());
        }
        tra.setStatus("1");
        tratamento.save(tra);
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public Optional<TratamentoModel> retornaTratamento(Integer id) {
        return tratamento.findById(id);
    }

    
}
