package com.gestao.aulas.jwt;

import java.util.Optional;
import com.gestao.aulas.model.UsuarioModel;
import com.gestao.aulas.repository.UsuarioRepository;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;


@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UsuarioRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        logger.info("load user...");
        Optional<UsuarioModel> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            logger.info("user:: {}", user.get().getUsername());
            return getJwtUser(user.get());
        } else {
            logger.info("user not found");
            return null;
        }
    }


    private JwtUser getJwtUser(UsuarioModel user) {
        Collection<GrantedAuthority> colecao = new ArrayList();
        colecao.add(new SimpleGrantedAuthority(user.getPerfil().getNome()));
    	          
    	return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getToken(),
                colecao
                
        );
    }
}
