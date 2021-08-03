package com.gestao.aulas.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class RoleModel {
    
    @Id
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "usuario_id")
    private Integer usuario_id;
    
    @Column(name = "perfil_id")
    private Integer perfil_id;
    
    
    @Column(name = "processo_id")
    private Integer processo_id;
  
    @Column(name = "processo")
    private String processo;


}