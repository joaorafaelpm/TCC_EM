package com.pendezzafood.pendezzapizza.domain.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {

    // """
        // id : uuid (pk)
        // nome : string (sem limites)
        // senha : string
        // email : string (unico)
        // dataCadastro : dateTime (format "yyyy-MM-dd')

        // TODO: adicionar os relacionamentos e criar classes relacionadas :

        // grupos : List<Grupo> (muitos para muitos)
        // pedidos : List<Pedido> (um para muitos)
        // historicoPedido : HistoricoPedido (um para um)
    // """

    @Id
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID() ;
    private String nome ; 
    private String senha ;    
    private String email ;    
    
    @CreationTimestamp
    @Column(columnDefinition = "datetime" , name = "data_cadastro" , nullable = false)
    private LocalDateTime dataCadastro ;

    @ManyToMany
    @JoinTable(name = "usuario_grupo" ,
            joinColumns = @JoinColumn(name = "usuario_id") ,
            inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private List<Grupo> grupos = new ArrayList<>();


}
