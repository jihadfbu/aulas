package com.gestao.aulas.resolver;

import com.gestao.aulas.model.PessoaModel;
import com.gestao.aulas.model.PraticaModel;
import com.gestao.aulas.components.Mailer;
import com.gestao.aulas.components.Mensagem;
import com.gestao.aulas.components.SendMail;
import com.gestao.aulas.model.CentroCustoModel;
import com.gestao.aulas.model.EntradasaidaModel;
import com.gestao.aulas.model.ExtratopessoaModel;
import com.gestao.aulas.model.FechamentoDiaModel;
import com.gestao.aulas.model.MatriculaModel;
import com.gestao.aulas.model.SaldoModel;
import com.gestao.aulas.model.TratamentoModel;
import com.gestao.aulas.model.TurmaModel;
import com.gestao.aulas.repository.PessoaRepository;
import com.gestao.aulas.repository.PraticaRepository;
import com.gestao.aulas.repository.CentroCustoRepository;
import com.gestao.aulas.repository.EntradasaidaRepository;
import com.gestao.aulas.repository.FechamentoDiaRepository;
import com.gestao.aulas.repository.MatriculaRepository;
import com.gestao.aulas.repository.SaldoRepository;
import com.gestao.aulas.repository.TratamentoRepository;
import com.gestao.aulas.repository.TurmaRepository;
import com.gestao.aulas.repository.UsuarioRepository;
import com.gestao.aulas.util.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

import java.sql.Timestamp;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.management.InvalidAttributeValueException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.security.access.prepost.PreAuthorize;
import com.gestao.aulas.util.Util;

@Service
@GraphQLApi
public class EntradasaidaService {

    @Autowired
    private EntradasaidaRepository entradasaida;

    @Autowired
    private MatriculaRepository matricula;

    @Autowired
    private TurmaRepository turma;

    @Autowired
    private TratamentoRepository tratamento;
    
    @Autowired
    private CentroCustoRepository centroCusto;
    
    @Autowired
    private PraticaRepository pratica;
    
    @Autowired
    private PessoaRepository pessoa;

    @Autowired
    private SaldoRepository saldo;

    @Autowired
    private UsuarioRepository usuario;

    @Autowired
    private FechamentoDiaRepository fr;
    
    @Autowired
    private Mailer mailer;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void salvarEntradasaida(
    String data,
    String forma_pagamento,
    Integer idPaciente,
    Integer id,
    String observacao,
    String tipo,
    Integer  idTratamento,
    Integer  idPratica,
    Integer  idCentroCusto,
    Float valor,
    Integer idTurma,
    Integer idPaciente2, Integer idUsuario,Integer idMatricula
    ) throws ParseException,InvalidAttributeValueException {
    	FechamentoDiaModel f = fr.findFirstByDataAndDtCancelamentoIsNull(Util.ConvStrToLocalDate(data));
    	if(f != null && (idPaciente != null && idPaciente != 0)) {
    		throw new InvalidAttributeValueException("->Caixa já fechado nesta data!");
    	}
    	if(idMatricula>0 && idTratamento>0) {
    		throw new InvalidAttributeValueException("->Não é possivel cadastrar com a turma e o tratamento setados juntos!");
    	}else
    	if(tipo.equals("C") && (idMatricula>0 || idTratamento>0)) {
    		throw new InvalidAttributeValueException("->Não é possivel cadastrar um crédito para uma turma ou tratamento");
    	}else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	        EntradasaidaModel entradaSM;
	        if(id == null || id == 0){
	            entradaSM = new EntradasaidaModel();
	            entradaSM.setDtCadastro(LocalDateTime.now().format(formatter));
	            entradaSM.setUsuarioCad(usuario.findById(idUsuario).get());
	       }else{
	            entradaSM = (entradasaida.findById(id)).get();
	            entradaSM.setDtUltimaEdt(LocalDateTime.now().format(formatter));
	            entradaSM.setUsuarioEdt(usuario.findById(idUsuario).get());
	       }
	       entradaSM.setData(data);
	       entradaSM.setForma_pagamento(forma_pagamento);
	       entradaSM.setObservacao(observacao);
	       if(idPaciente != null && idPaciente != 0){
	    	   entradaSM.setPaciente(pessoa.findById(idPaciente).get());
	    	   
	       }
	       entradaSM.setTipo(tipo);
	       if(idTratamento==null || idTratamento==0){
	           entradaSM.setTratamento(null);
	       }else{
	            entradaSM.setTratamento(tratamento.findById(idTratamento).get());
	       }
	       if((idCentroCusto==null || idCentroCusto==0)){
	    	   entradaSM.setCentroCusto(null);
	       }else{
	    	   if((idPaciente == null || idPaciente == 0)) {
	    		   entradaSM.setCentroCusto(centroCusto.findById(idCentroCusto).get());	    		   	    		   
	    	   }else {
		    	   entradaSM.setCentroCusto(null);
	    	   }
	       }
	       
	       /*
	       if(idTurma == null || idTurma == 0   ){
	           entradaSM.setTurma(null);
	       }else{
	    	   TurmaModel t = turma.findById(idTurma).get();
	    	   if(t.getPratica().getPercentual()>0) {
	    		   EntradasaidaModel entradaSMTer = new EntradasaidaModel();
	    		   entradaSMTer.setData(data);
	    		   entradaSMTer.setPaciente(t.getInstrutor());
	    		   entradaSMTer.setTipo("C");
	    		   entradaSMTer.setTurma(t);
	    		   entradaSMTer.setValor(valor*t.getPratica().getPercentual()/100);
	   	        	entradaSMTer.setDtCadastro(LocalDateTime.now().format(formatter));
	   	        	entradaSMTer.setUsuarioCad(usuario.findById(idUsuario).get());
	    		   entradasaida.save(entradaSMTer);
	    	   }
	    	   entradaSM.setTurma(t);
	       }
	       */
	       if(idPratica==null || idPratica==0){
	           entradaSM.setPratica(null);
	       }else{
	            entradaSM.setPratica(pratica.findById(idPratica).get());
	       }
	       entradaSM.setValor(valor);
	       MatriculaModel m=null;
	       if(idMatricula == null || idMatricula == 0   ){
	           entradaSM.setMatricula(null);
	       }else{
	    	   m = matricula.findById(idMatricula).get();
	    	   entradaSM.setMatricula(m);
	       }
	       entradaSM = entradasaida.save(entradaSM);
	       if(idMatricula>0) {
	    	   if(m.getTurma().getPratica().getPercentual()>0) {
	    		   EntradasaidaModel entradaSMTer = new EntradasaidaModel();
	    		   entradaSMTer.setData(data);
	    		   entradaSMTer.setPaciente(m.getTurma().getInstrutor());
	    		   entradaSMTer.setTipo("C");
	    		   entradaSMTer.setMatricula(m);
	    		   entradaSMTer.setValor(valor*m.getTurma().getPratica().getPercentual()/100);
	   	        	entradaSMTer.setDtCadastro(LocalDateTime.now().format(formatter));
	   	        	entradaSMTer.setUsuarioCad(usuario.findById(idUsuario).get());
	   	        	entradaSMTer.setPacienteEntradaSaida(entradaSM);
	    		   entradasaida.save(entradaSMTer);
	    	   }	    	   
	       }
	       if(tipo.equals("C")) {
	    	  if(idPaciente != null && idPaciente != 0) {
		    	   String email = entradaSM.getPaciente().getEmail();
		    	   if(!email.isEmpty())
		    		   mailer.enviar(new Mensagem("Mahananda <sis.mahananda@gmail.com>", 
	    				   Arrays.asList(email)
	    				   , "Novo pagamento", "Shanti,\n\n Contabilizamos um novo crédito no seu cadastro de R$"+valor+".\n\n\n Agradecemos pela confiança! \n\n Ashram Mahananda"));
	    	   }
	    	   if(forma_pagamento.equals("RE")) {
		    	   EntradasaidaModel entradaS = new EntradasaidaModel();
	    		   entradaS.setData(data);
	    		   entradaS.setTipo("D");
	    		   entradaS.setValor(valor);
	    		   entradaS.setObservacao(observacao);
	    		   entradaS.setCentroCusto(centroCusto.findById(idCentroCusto).get());
	   	           entradaS.setDtCadastro(LocalDateTime.now().format(formatter));
	   	           entradaS.setUsuarioCad(usuario.findById(idUsuario).get());
	    		   entradaS.setEntradaSaida(entradaSM);
	    		   entradasaida.save(entradaS);

	    	   }else {
	    		   if(idPaciente != null && idPaciente != 0) {
	    			   EntradasaidaModel entradaS = new EntradasaidaModel();
		    		   entradaS.setData(data);
		    		   entradaS.setTipo("C");
		    		   entradaS.setValor(valor);
		    		   entradaS.setObservacao(observacao);
		    		   if(idCentroCusto != null && idCentroCusto != 0) {
		    			   entradaS.setCentroCusto(centroCusto.findById(idCentroCusto).get());
		    		   }
		   	           entradaS.setDtCadastro(LocalDateTime.now().format(formatter));
		   	           entradaS.setUsuarioCad(usuario.findById(idUsuario).get());
		    		   entradaS.setEntradaSaida(entradaSM);
		    		   entradasaida.save(entradaS);
	    		   }
	    	   }
	       }else {
	    	   if(idPaciente2 != null && idPaciente2 != 0) {
	    		   EntradasaidaModel entradaS = new EntradasaidaModel();
	    		   entradaS.setPaciente(pessoa.findById(idPaciente2).get());
	    		   entradaS.setData(data);
	    		   if(idPaciente != null && idPaciente != 0) {
	    			   entradaS.setTipo("C");	    			   
	    		   }else {
	    			   entradaS.setTipo("D");	    			   
	    		   }
	    		   entradaS.setValor(valor);
	    		   entradaS.setObservacao(observacao);
	    		   entradaS.setEntradaSaida(entradaSM);
	   	           entradaS.setDtCadastro(LocalDateTime.now().format(formatter));
	   	           entradaS.setUsuarioCad(usuario.findById(idUsuario).get());
	    		   entradasaida.save(entradaS);
	    	   }
	    	   
	       }
    	}
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GraphQLQuery()
    public List<EntradasaidaModel> listarCaixa(String dataIni,String dataFim,Integer idPaciente) {   	
       	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntradasaidaModel> q = cb.createQuery(EntradasaidaModel.class);
        Root<EntradasaidaModel> from = q.from(EntradasaidaModel.class);
    	q.select(from);
    	
    	Predicate predicate = cb.and();
    	predicate = cb.and(predicate, cb.isNull(from.get("paciente")));
    	if(!dataIni.isEmpty() && dataIni!=null) {
    		LocalDate Ndata = Util.ConvStrToLocalDate(dataIni);
    		if(!dataFim.isEmpty() && dataFim!=null) {
        		LocalDate Ndata2 = Util.ConvStrToLocalDate(dataFim);
    			predicate = cb.and(predicate, cb.between(from.get("data"), Ndata,Ndata2));    			    			
    		}else {
    			predicate = cb.and(predicate, cb.greaterThan(from.get("data"), Ndata));
    		}
    	}
    	if (idPaciente != null && idPaciente > 0){
    	     predicate = cb.and(predicate, cb.equal(from.get("entradaSaida").get("paciente"), pessoa.findById(idPaciente).get()));
    	}
    	
    	TypedQuery<EntradasaidaModel> typedQuery = entityManager.createQuery(
    	    q.select(from )
    	    .where( predicate )
    	    .orderBy(cb.desc(from.get("data")))
    	);
    	List<EntradasaidaModel>  aux=typedQuery.getResultList();
    	for (int i = 0; i < aux.size(); i++) {
    		EntradasaidaModel l = entradasaida.findByEntradaSaida(entradasaida.findById(aux.get(i).getId()).get());
    		if(l!=null) {
    			aux.get(i).setFilho(l);
    			
    		}
		}
    	return aux;    
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<EntradasaidaModel> listarCaixaByData(String date) {
    	LocalDate Ndata = Util.ConvStrToLocalDate(date);
    	//return entradasaida.findByPacienteIsNullAndDataOrderByIdDesc(Ndata);
    	List<EntradasaidaModel> aux = entradasaida.findByPacienteIsNullAndDataOrderByDataDescIdDesc(Ndata);;
    	for (int i = 0; i < aux.size(); i++) {
    		EntradasaidaModel l = entradasaida.findByEntradaSaida(entradasaida.findById(aux.get(i).getId()).get());
    		if(l!=null) {
    			aux.get(i).setFilho(l);
    			
    		}
		}
    	return aux;
    	
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GraphQLQuery()
    public List<EntradasaidaModel> listarCaixaByPeriodo(String date1,String date2) {
    	LocalDate Ndata1 = Util.ConvStrToLocalDate(date1);
    	List<EntradasaidaModel> aux;
    	if(date2.isEmpty()) {
    		aux = entradasaida.findByPacienteIsNullAndDataOrderByDataDescIdDesc(Ndata1);
    	}else {
    		LocalDate Ndata2 = Util.ConvStrToLocalDate(date2);    		
    		aux = entradasaida.findByPacienteIsNullAndDataBetweenOrderByDataDesc(Ndata1, Ndata2);    		    		
    	}
    	for (int i = 0; i < aux.size(); i++) {
    		EntradasaidaModel l = entradasaida.findByEntradaSaida(entradasaida.findById(aux.get(i).getId()).get());
    		if(l!=null) {
    			aux.get(i).setFilho(l);
    			
    		}
		}
    	return aux;
    }
    
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<EntradasaidaModel> listarEntradasaidasByPaciente(PessoaModel paciente) {
    	//return entradasaida.findByPacienteOrderByDataDesc(paciente);
    	List<EntradasaidaModel> aux = entradasaida.findByPacienteOrderByDataDescIdDesc(paciente);
    	for (int i = 0; i < aux.size(); i++) {
    		EntradasaidaModel l = entradasaida.findByEntradaSaida(aux.get(i));
    		if(l!=null) {
    			aux.get(i).setFilho(l);
    		}
		}
    	return aux;
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<EntradasaidaModel> listarEntradasaidasByTratamnento(TratamentoModel tratamento) {
        return entradasaida.findByTratamentoOrderByIdDesc(tratamento);
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<EntradasaidaModel> listarEntradasaidasByData(String data) {
    	
    	return entradasaida.findByPacienteIsNotNullAndDataOrderByIdDesc(Util.ConvStrToLocalDate(data));
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<SaldoModel> listarSaldos() {
        return saldo.findSaldos();
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<SaldoModel> listarSaldosByDataCorte(String data) {
        return saldo.findSaldosByDataCorte(Util.ConvStrToLocalDate(data));
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<CentroCustoModel> listarCentrosCusto() {
    	return centroCusto.findAll();
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public SaldoModel listarSaldoByPaciente(Integer id) {
        return saldo.findSaldoByPaciente(id);
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public SaldoModel listarSaldoByPacienteAndDataCorte(Integer id,String data) {
        return saldo.findSaldoByPacienteAndDatacorte(id, Util.ConvStrToLocalDate(data));
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<ExtratopessoaModel> extratoPessoa(Integer idPessoa, String dataIni, String dataFim) {
    	List<Object[]> aux  =entradasaida.extratoPeriodo(idPessoa, dataIni, dataFim);
    	List<ExtratopessoaModel> lex = new ArrayList<>();
    	for(Object[] a :aux) {
    		ExtratopessoaModel ex = new ExtratopessoaModel();
    		ex.setId((String) (a[0]) );
    		ex.setProd((String) (a[2]) );
    		ex.setTipo((String) (a[4].toString()) );
    		ex.setTotal((Double) (a[3]) );
    		
    		ex.setDe((String) (a[5]) );
    		ex.setPara((String) (a[6]) );
    		ex.setNome((String) (a[1]) );
    		lex.add(ex);    		
    	}
    	return lex;
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<ExtratopessoaModel> extratoPessoaDetalhado(PessoaModel idPessoa, String dataIni, String dataFim) {
    	List<EntradasaidaModel> aux ;
    	if(dataFim.isEmpty() && dataIni.isEmpty()) {
    		aux  = entradasaida.findByPacienteOrderByDataDescIdDesc(idPessoa);
    	}else {
    		aux  = entradasaida.findByPacienteAndDataBetweenOrderByDataDescIdDesc(idPessoa, Util.ConvStrToLocalDate(dataIni),Util.ConvStrToLocalDate(dataFim));    		
    	}
    	List<ExtratopessoaModel> lex = new ArrayList<>();
    	for (int i = 0; i < aux.size(); i++) {
    		ExtratopessoaModel ex = new ExtratopessoaModel();
    		ex.setDataini((String) aux.get(i).getData());
    		ex.setId((String) aux.get(i).getId().toString() );
    		String prod="";
    		if(aux.get(i).getTratamento() != null) {
    			prod = aux.get(i).getTratamento().getPratica().getNome();
    		}
    		if(aux.get(i).getPratica() != null) {
    			prod = aux.get(i).getPratica().getNome();
    		}
    		if(aux.get(i).getMatricula() != null) {
    			prod = aux.get(i).getMatricula().getTurma().getPratica().getNome();
    		}
    		
    		ex.setProd((String) prod );
    		ex.setTipo((String) aux.get(i).getTipo() );
    		ex.setTotal((Double)  Double.parseDouble(aux.get(i).getValor().toString()));
    		ex.setObservacao((String) aux.get(i).getObservacao() );
    		ex.setNome((String) aux.get(i).getPaciente().getNome() );
    		if(aux.get(i).getMatricula() != null) {
				if(aux.get(i).getTipo().equals("C")) {
    					ex.setPaciente((String) aux.get(i).getMatricula().getPaciente().getNome() );
				}
    		}

    		if(aux.get(i).getTratamento() != null) {
				if(aux.get(i).getTipo().equals("C")) {
    					ex.setPaciente((String) aux.get(i).getTratamento().getPaciente().getNome() );
				}
    		}
    		if(aux.get(i).getEntradaSaida() !=null) {
    			ex.setDe((String) aux.get(i).getEntradaSaida().getPaciente().getNome() );
    			
    		}
    		EntradasaidaModel esPara =entradasaida.findByEntradaSaida(aux.get(i)); 
    		if(esPara != null) {
    			if(esPara.getPaciente() != null) {
    				ex.setPara((String) esPara.getPaciente().getNome() );
    			}
    		}
    		
    		lex.add(ex);    
    	}
    	return lex;
    }
    
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<ExtratopessoaModel> extratoGeral(Integer dias) {
    	List<Object[]> aux  =entradasaida.extratoPeriodoGeral(dias);
    	List<ExtratopessoaModel> lex = new ArrayList<>();
    	for(Object[] a :aux) {
    		ExtratopessoaModel ex = new ExtratopessoaModel();
    		ex.setDataini((String) Util.ConvTimeToStr((Timestamp) (a[4])));
    		ex.setId((String) (a[0]) );
    		ex.setProd((String) (a[1]) );
    		ex.setTipo((String) (a[3]) );
    		ex.setTotal((Double) (a[2]) );
    		
    		lex.add(ex);    		
    	}
    	return lex;
    }

    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void excluirEntradasaida(Integer id) throws InterruptedException, InvalidAttributeValueException{
    	EntradasaidaModel es = entradasaida.findById(id).get();
    	FechamentoDiaModel f = fr.findFirstByDataAndDtCancelamentoIsNull(Util.ConvStrToLocalDate(es.getData()));
    	if(f != null) {
    		throw new InvalidAttributeValueException("->Caixa já fechado nesta data!");
    	}
    	EntradasaidaModel esP = entradasaida.findByEntradaSaida(es);
    	if(esP != null) {
    		entradasaida.deleteById(esP.getId());
    	}
    	
    	EntradasaidaModel esTer = entradasaida.findByPacienteEntradaSaida(es);
    	if(esTer != null) {
    		entradasaida.deleteById(esTer.getId());
    	}

    	
    	if(es.getMatricula()!= null) {
    		if(es.getTipo().equals("C")) {
    			throw new InterruptedException("->Não possível excluir um crédito de uma turma, deve ser excluído o débito");
    		}
			entradasaida.deleteById(id);
    		
    	}else if(es.getTratamento()!= null) {
    		if(es.getTipo().equals("C")) {
    			throw new InterruptedException("->Não possível excluir um crédito de um tratamento, deve ser excluído o débito");
    		}else {
    			List<EntradasaidaModel> esT = entradasaida.findByTratamentoOrderByIdDesc(es.getTratamento());
    			for (EntradasaidaModel T : esT) {
					entradasaida.deleteById(T.getId());
				}
    		}
    	}else {
    		entradasaida.deleteById(id);
    	}
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public Optional<EntradasaidaModel> retornaEntradasaida(Integer id) {
    	if(id!=0 && id != null) {
    		Optional<EntradasaidaModel> es = entradasaida.findById(id);
	    	EntradasaidaModel esFilho = entradasaida.findByEntradaSaida(es.get()); 
			if(esFilho != null) {
				if(esFilho.getPaciente() != null) {
					es.get().setFilho(esFilho);
				}
			}
			return es;
    	}
    	return null;
    	/*EntradasaidaModel esPai =entradasaida.findByEntradaSaida(es.get()); 
		if(esPai != null) {
			if(esPai.getPaciente() != null) {
				es.get().setEntradaSaida(esPai);
			}
		}*/		
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GraphQLQuery()
    public List<EntradasaidaModel> listarMovimentacaoPaciente(Integer idPaciente,String dataIni,String dataFim,ArrayList<Integer> idProdutos,ArrayList<Integer> idTratamentos, ArrayList<String> formaPagamento, String tipo) {
    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<EntradasaidaModel> q = cb.createQuery(EntradasaidaModel.class);
        Root<EntradasaidaModel> from = q.from(EntradasaidaModel.class);
    	q.select(from);
    	
    	Predicate predicate = cb.and();
    	predicate = cb.and(predicate, cb.isNotNull(from.get("paciente")));
    	if(idProdutos!=null) {
    		if(idProdutos.size()>0) {
    			ArrayList<PraticaModel> prod= new ArrayList<>();
    			for (Integer i: idProdutos) {
    				prod.add(pratica.findById(i).get());
    			}
    			predicate = cb.and(predicate, cb.in(from.get("pratica")).value(prod));
    		}
    	}
    	if(formaPagamento!=null) {
    		if(formaPagamento.size()>0) {
    			predicate = cb.and(predicate, cb.in(from.get("forma_pagamento")).value(formaPagamento));
    		}
    	}
    	if (!tipo.equals("") ){
   	     predicate = cb.and(predicate, cb.equal(from.get("tipo"), tipo));
   	}
    	if(idTratamentos!=null) {
    		if(idTratamentos.size()>0) {
    			ArrayList<PraticaModel> tra= new ArrayList<>();
    			idTratamentos.forEach((i)->{
    				PraticaModel t = pratica.findById(i).get();
    				tra.add(t);
    			});
    			predicate = cb.and(predicate, cb.in(from.get("tratamento").get("pratica")).value(tra));
    		}
    	}
    	
    	if(!dataIni.isEmpty() && dataIni!=null) {
    		LocalDate Ndata = Util.ConvStrToLocalDate(dataIni);
    		if(dataFim!=null) {
        		LocalDate Ndata2 = Util.ConvStrToLocalDate(dataFim);
    			predicate = cb.and(predicate, cb.between(from.get("data"), Ndata,Ndata2));    			    			
    		}else {
    			predicate = cb.and(predicate, cb.greaterThan(from.get("data"), Ndata));
    		}
    	}
    	if (idPaciente != null && idPaciente > 0){
    	     predicate = cb.and(predicate, cb.equal(from.get("paciente"), pessoa.findById(idPaciente).get()));
    	}
    	List<Order> orderList = new ArrayList();
    	orderList.add(cb.desc(from.get("data")));
    	orderList.add(cb.desc(from.get("tipo")));
    	TypedQuery<EntradasaidaModel> typedQuery = entityManager.createQuery(
    	    q.select(from )
    	    .where( predicate )
    	    .orderBy(orderList)
    	);
    	return typedQuery.getResultList();    }

}
