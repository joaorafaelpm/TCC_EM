package com.pendezzafood.pendezzapizza.domain.model ;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Herança no mesmo esquema de tabela
@EqualsAndHashCode(callSuper = true)
public class HistoricoPedido extends Historico {
    
    @EqualsAndHashCode.Include
    private UUID usuario_id ;

    @JsonIgnore
    @OneToMany(mappedBy = "usuario" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Pedido> pedidos ;

    public HistoricoPedido (UUID id , LocalDate date , UUID usuario_id ) {
        super(id , date) ;
        this.usuario_id = usuario_id ;
        this.pedidos = new ArrayList<>() ;
    }
}
