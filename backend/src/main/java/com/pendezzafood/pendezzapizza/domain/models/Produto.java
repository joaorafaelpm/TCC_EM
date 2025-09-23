package com.pendezzafood.pendezzapizza.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id ;

    private String nome ;
    private String descricao ;
    private BigDecimal preco ;
    private Boolean ativo = false;

    @ManyToOne
    @JoinColumn(name = "restaurante_id" , nullable = false)
//    @JsonIgnore
    private Restaurante restaurante;




}
