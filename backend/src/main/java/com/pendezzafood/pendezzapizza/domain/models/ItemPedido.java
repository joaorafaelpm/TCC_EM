package com.pendezzafood.pendezzapizza.domain.models;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class ItemPedido {

    @Id
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID() ;

    private Integer quantidade ;
    private BigDecimal precoUnitario ;
    private BigDecimal precoTotal ;
    private String observacao ;

    @ManyToOne
    @JoinColumn(name = "pedido_id" , nullable = false)
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id" , nullable = false)
    private Produto produto;



}
