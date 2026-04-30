/**
 * @summary     Representa a entidade de estado (unidade federativa), mantendo a relação
 *              com as cidades pertencentes a ele.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidade JPA que representa um estado do país.
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos State
public class State implements Serializable {

    /**
     * Identificador único do estado.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos State, evitando comparações incorretas baseadas em outros atributos mutáveis.
     * {@code @JdbcTypeCode(SqlTypes.BINARY)} — instrui o Hibernate a tratar o UUID como bytes binários no JDBC, compatível com a definição BINARY(16) no banco.
     * {@code @Column(columnDefinition = "BINARY(16)")} — armazena o UUID como 16 bytes no banco em vez de uma string de 36 caracteres, economizando espaço e melhorando performance em índices.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    /**
     * Lista de cidades pertencentes a este estado.
     * Anotada com {@code @JsonIgnore} para evitar serialização recursiva infinita,
     * já que {@code City} referencia {@code State} e {@code State} referencia {@code City}.
     * Mapeamento somente leitura — a cidade é responsável por declarar o lado dono
     * da relação via {@code mappedBy = "state"} em {@code City}.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "state")
    private List<City> cities = new ArrayList<>();

    /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
    @UpdateTimestamp
    private OffsetDateTime updateDate;
}