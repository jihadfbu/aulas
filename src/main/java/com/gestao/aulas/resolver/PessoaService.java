
package com.gestao.aulas.resolver;

import com.gestao.aulas.model.PessoaModel;
import com.gestao.aulas.model.PessoaniverModel;
import com.gestao.aulas.repository.PessoaRepository;
import com.gestao.aulas.repository.PessoaniverRepository;
import com.gestao.aulas.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
@Service
@GraphQLApi
public class PessoaService{

    @Autowired
    private PessoaRepository pessoa;

    @Autowired
    private PessoaniverRepository pessoaniver;

    @Autowired
    private UsuarioRepository usuario;
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void salvarPessoa(String nome,
            String celular,
            String complemento,
            String endereco,
            String email,
            String numero,
            String telefone1,
            String telefone2,
            String nascimento,
            Integer idResponsavel,
            String telefone3,
            String cpf,
            String terapeuta,
            String rg,
            Integer id,
            String observacao, Integer idUsuario
    ){
        PessoaModel pes;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
       if(id == null || id == 0){
            pes = new PessoaModel();
            pes.setDtCadastro(LocalDateTime.now().format(formatter));
            pes.setUsuarioCad(usuario.findById(idUsuario).get());
       }else{
            pes = (pessoa.findById(id)).get();
            pes.setDtUltimaEdt(LocalDateTime.now().format(formatter));
            pes.setUsuarioEdt(usuario.findById(idUsuario).get());
       }
       pes.setNome(nome);
       pes.setCelular(celular);
       pes.setComplemento(complemento);
       pes.setEndereco(endereco);
       pes.setEmail(email);
       pes.setDtNascimento(nascimento);
       pes.setNumero(numero);
       pes.setCpf(cpf);
       pes.setRg(rg);
       pes.setTerapeuta(terapeuta);
       pes.setObservacao(observacao);
       if(idResponsavel!= null && idResponsavel!=0){
            PessoaModel resp;
            resp = (pessoa.findById(idResponsavel)).get();
            pes.setResponsavel(resp);
           
       }else{
            pes.setResponsavel(null);
           
       }
       pes.setTelefone1(telefone1);
       pes.setTelefone2(telefone2);
       pes.setTelefone3(telefone3);
       pessoa.save(pes);
       
    } 

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLMutation()
    public void excluirPessoa(Integer id){
        pessoa.deleteById(id);
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<PessoaModel> listarPessoas(){
        return pessoa.findAll(Sort.by("nome").ascending());
    }
    
    @PreAuthorize("hasRole('SECRETARIO')|| hasRole('ADMIN')")
    @GraphQLQuery()
    public List<PessoaniverModel> listarNiver(){
        return pessoaniver.findNiver();
    }
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<PessoaModel> listarInstrutores(){
        return pessoa.findByTerapeutaOrderByNomeAsc("1");
    }

    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public Optional<PessoaModel> retornaPessoa(Integer id){
        return pessoa.findById(id);
    }
}