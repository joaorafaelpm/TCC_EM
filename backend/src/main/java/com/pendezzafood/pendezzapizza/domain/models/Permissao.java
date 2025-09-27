package com.pendezzafood.pendezzapizza.domain.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Permissao {

    @Id
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID() ;

    private String nome ;

    private String descricao ;

    public Permissao (String nome , String descricao) {
        this.nome = nome ;
        this.descricao = descricao ;
    }

}
