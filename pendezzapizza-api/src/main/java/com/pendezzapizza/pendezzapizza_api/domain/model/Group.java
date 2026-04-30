/**
 * @summary     Representa a entidade de grupo de usuários, que agrupa um conjunto de permissões
 *              para facilitar o controle de acesso na aplicação.
 * @difficulty  Low
 * @depends-on  Permission
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entidade JPA que representa um grupo de acesso.
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 *
 * A tabela é nomeada explicitamente como {@code `group`} pois "group" é uma
 * palavra reservada em SQL — sem as aspas, o banco rejeitaria a criação da tabela.
 */
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos Group
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`group`")
public class Group implements Serializable {

    /**
     * Identificador único do grupo.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos Group, evitando comparações incorretas baseadas em outros atributos mutáveis.
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

    /**
     * Conjunto de permissões associadas a este grupo.
     * O {@code @ManyToMany} indica que um grupo pode ter várias permissões e uma mesma
     * permissão pode pertencer a vários grupos simultaneamente.
     * O {@code @JoinTable} define a tabela intermediária {@code group_permission} que
     * gerencia essa associação no banco, evitando redundância de dados.
     * Inicializado como {@code HashSet} para garantir que não haja permissões duplicadas no conjunto.
     */
    @ManyToMany
    @JoinTable(name = "group_permission",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permission = new HashSet<>(); // Entidade de permissão — consulte Permission para detalhes do mapeamento

    /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
    @UpdateTimestamp
    private OffsetDateTime updateDate;

    /**
     * Associa uma permissão a este grupo.
     *
     * @param permission permissão a ser adicionada ao grupo
     * @return {@code true} se a permissão foi adicionada; {@code false} se já existia no conjunto
     */
    public boolean associatePermission(Permission permission) {
        return getPermission().add(permission);
    }

    /**
     * Remove a associação de uma permissão deste grupo.
     *
     * @param permission permissão a ser removida do grupo
     * @return {@code true} se a permissão foi removida; {@code false} se não estava no conjunto
     */
    public boolean disassociatePermission(Permission permission) {
        return getPermission().remove(permission);
    }
}