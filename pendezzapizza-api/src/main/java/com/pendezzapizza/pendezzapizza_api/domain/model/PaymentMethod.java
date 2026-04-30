/**
 * @summary     Representa uma forma de pagamento disponível na aplicação,
 *              como cartão de crédito, dinheiro ou Pix.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.domain.model;

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
import java.util.UUID;

/**
 * Entidade JPA que representa uma forma de pagamento cadastrada no sistema.
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos PaymentMethod
public class PaymentMethod implements Serializable {

    /**
     * Identificador único da forma de pagamento.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos PaymentMethod, evitando comparações incorretas baseadas em outros atributos mutáveis.
     * {@code @JdbcTypeCode(SqlTypes.BINARY)} — instrui o Hibernate a tratar o UUID como bytes binários no JDBC, compatível com a definição BINARY(16) no banco.
     * {@code @Column(columnDefinition = "BINARY(16)")} — armazena o UUID como 16 bytes no banco em vez de uma string de 36 caracteres, economizando espaço e melhorando performance em índices.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    /** Rótulo legível da forma de pagamento exibido ao usuário (ex: "Cartão de Crédito", "Pix"). */
    @Column(nullable = false)
    private String description;

    /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
    @UpdateTimestamp
    private OffsetDateTime updateDate;
}