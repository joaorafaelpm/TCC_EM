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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // Herança no mesmo esquema de tabela
@EqualsAndHashCode(callSuper = true)
public class HistoricoRestaurante extends Historico {
    
    @EqualsAndHashCode.Include
    private UUID restaurante_id ;

    
    @JsonIgnore
    @OneToMany(mappedBy = "restaurante" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Pedido> pedidosEntregues ;

    public HistoricoRestaurante (UUID id , LocalDate date , UUID restaurante_id ) {
        super(id , date);
        this.restaurante_id = restaurante_id ;
        this.pedidosEntregues = new ArrayList<>() ;
    }
}
