package com.gestao.aulas.repository;

import com.gestao.aulas.model.UsuarioModel;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;



public interface UsuarioRepository extends JpaRepository<UsuarioModel, Integer> {

    public Object findAllById(Integer idResponsavel);
    
    Optional<UsuarioModel> findByUsername(String username);

}