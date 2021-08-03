package com.gestao.aulas.resolver;

import com.gestao.aulas.repository.PerfilRepository;
import com.gestao.aulas.model.UsuarioModel;
import com.gestao.aulas.repository.UsuarioRepository;
import io.leangen.graphql.annotations.GraphQLArgument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.gestao.aulas.jwt.JwtTokenUtil;
import org.springframework.security.access.prepost.PreAuthorize;


@Service
@GraphQLApi
public class UsuarioService{

    @Autowired
    private UsuarioRepository pessoa;
    @Autowired
    private PerfilRepository perfil;
    @Autowired
     private JwtTokenUtil jwtTokenUtil;

    @GraphQLMutation()
    public void salvarUsuario(
            String username,
            String password,
            Integer perfil_id,
            Integer id
    ){
       UsuarioModel pes = new UsuarioModel();   
       if(id != null){
        pes = (pessoa.findById(id)).get();
           
       }
       BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
       pes.setPerfil((perfil.findById(perfil_id)).get());
       pes.setPassword(encoder.encode(password));
       pes.setUsername(username);
       pessoa.save(pes);
    } 
    
    @PreAuthorize("hasRole('SECRETARIO') || hasRole('ADMIN')")
    @GraphQLQuery()
    public List<UsuarioModel> listarUsuarios(){
        return pessoa.findAll();
    }

    @GraphQLQuery()
    public Optional<UsuarioModel> retornaUsuario(Integer id){
        return pessoa.findById(id);
    }
    
    @GraphQLMutation(description = "login de usuário")
    public String login(@GraphQLArgument(name = "username") String username,
                         @GraphQLArgument(name = "password") String password) throws InvalidCredentialsException {
        
        Optional<UsuarioModel> user = pessoa.findByUsername(username);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(user.isPresent()) {
            if (encoder.matches(password, user.get().getPassword())) {
                return jwtTokenUtil.generateToken(user.get());
            } else {
                throw new InvalidCredentialsException("->Senha inválida!");
            }
        }else{
            throw new InvalidCredentialsException("->login inválido!");
        }
    }

    @GraphQLMutation(description = "troca de senha")
    public void trocarSenha(String senha,Integer id) {
        UsuarioModel u;
        u = (pessoa.findById(id)).get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        u.setPassword(encoder.encode(senha));
        pessoa.save(u);
        
    }
}