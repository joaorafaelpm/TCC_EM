/**
 * @summary     Representa um produto do cardápio vinculado a um restaurante,
 *              com controle de ativação e desativação para exibição ao cliente.
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
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Entidade JPA que representa um produto do cardápio.
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos Product
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

    /**
     * Identificador único do produto.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos Product, evitando comparações incorretas baseadas em outros atributos mutáveis.
     * {@code @JdbcTypeCode(SqlTypes.BINARY)} — instrui o Hibernate a tratar o UUID como bytes binários no JDBC, compatível com a definição BINARY(16) no banco.
     * {@code @Column(columnDefinition = "BINARY(16)")} — armazena o UUID como 16 bytes no banco em vez de uma string de 36 caracteres, economizando espaço e melhorando performance em índices.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String description;
    private BigDecimal price;

    /**
     * Indica se o produto está disponível para exibição e pedidos.
     * Inicializado como {@code true} para que todo produto recém-cadastrado
     * já esteja ativo por padrão, sem necessidade de ativação manual.
     */
    private Boolean active = true;

    /** Restaurante ao qual este produto pertence. Um produto não pode existir sem um restaurante. */
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
    @UpdateTimestamp
    private OffsetDateTime updateDate;

    /**
     * Ativa o produto, tornando-o disponível para exibição e pedidos.
     * Prefira verificar {@code canActivate()} antes de chamar este método
     * para evitar ativações redundantes.
     */
    public void activate() { this.active = true; }

    /**
     * Desativa o produto, removendo-o da exibição e impedindo novos pedidos.
     * Prefira verificar {@code canDeactivate()} antes de chamar este método
     * para evitar desativações redundantes.
     */
    public void deactivate() { this.active = false; }

    /**
     * Verifica se o produto está disponível para exibição e pedidos.
     *
     * @return {@code true} se o produto estiver ativo
     */
    public boolean isActive() { return this.active; }

    /**
     * Verifica se o produto está indisponível para exibição e pedidos.
     *
     * @return {@code true} se o produto estiver inativo
     */
    public boolean isInactive() { return !isActive(); }

    /**
     * Indica se o produto pode ser ativado, ou seja, se atualmente está inativo.
     *
     * @return {@code true} se a ativação for permitida
     */
    public boolean canActivate() { return isInactive(); }

    /**
     * Indica se o produto pode ser desativado, ou seja, se atualmente está ativo.
     *
     * @return {@code true} se a desativação for permitida
     */
    public boolean canDeactivate() { return isActive(); }
}