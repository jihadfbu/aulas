/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestao.aulas.repository;

import com.gestao.aulas.model.PerfilModel;

import org.springframework.data.jpa.repository.JpaRepository;



public interface PerfilRepository extends JpaRepository<PerfilModel, Integer> {

}
