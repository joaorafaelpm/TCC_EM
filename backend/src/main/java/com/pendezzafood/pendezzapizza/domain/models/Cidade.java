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
public class Cidade {

        @Id
        @EqualsAndHashCode.Include
        @Column(columnDefinition = "BINARY(16)")
        private UUID id = UUID.randomUUID() ;

        private String nome ;

        @ManyToOne
        @JoinColumn(name = "estado_id" , nullable = false)
        private Estado estado ;

}
