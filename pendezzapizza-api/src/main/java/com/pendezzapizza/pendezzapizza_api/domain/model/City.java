package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class City {

        @Id
        @GeneratedValue
        @EqualsAndHashCode.Include
        @JdbcTypeCode(SqlTypes.BINARY)
        @Column(columnDefinition = "BINARY(16)")
        private UUID id;


        @Column(nullable = false)
        private String name ;

        @Valid
        @ManyToOne
        @JoinColumn(name = "state_id" , nullable = false)
        private State state ;

}
