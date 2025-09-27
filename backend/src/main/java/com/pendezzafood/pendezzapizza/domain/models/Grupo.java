package com.pendezzafood.pendezzapizza.domain.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Grupo {

    @Id
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID() ;

    private String nome ;

    @ManyToMany
    @JoinTable(name = "grupo_permissao" ,
            joinColumns = @JoinColumn(name = "grupo_id") ,
            inverseJoinColumns = @JoinColumn(name = "permissao_id"))
    private List<Permissao> permissoes = new ArrayList<>();

}
