/**
 * @summary     Representa a entidade de cidade no domínio da aplicação, mapeada para uma tabela
 *              no banco de dados. Associa cada cidade a um estado e rastreia a data de atualização.
 * @difficulty  Low
 * @depends-on  State
 */
package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidade JPA que representa uma cidade.
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos City
public class City implements Serializable {

        /**
         * Identificador único da cidade.
         *
         * {@code @Id} — marca este campo como chave primária da tabela.
         * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
         * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos City, evitando comparações incorretas baseadas em outros atributos mutáveis.
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
         * Entidade de estado à qual esta cidade pertence.
         * O {@code @ManyToOne} indica que muitas cidades podem pertencer ao mesmo estado.
         * O {@code @Valid} garante que as validações definidas dentro do objeto State
         * também sejam executadas ao validar um City.
         */
        @Valid
        @ManyToOne
        @JoinColumn(name = "state_id", nullable = false)
        private State state; // Entidade de estados — consulte State para detalhes do mapeamento

        /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
        @UpdateTimestamp
        private OffsetDateTime updateDate;
}