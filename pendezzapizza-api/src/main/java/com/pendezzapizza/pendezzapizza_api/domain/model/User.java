/**
 * @summary     Representa a entidade de usuário da plataforma, gerenciando credenciais de acesso
 *              e associações com grupos de permissão.
 * @difficulty  Medium
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entidade JPA que representa um usuário cadastrado na plataforma.
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 *
 * A tabela é nomeada explicitamente como {@code `user`} pois "user" é uma
 * palavra reservada em SQL — sem as aspas, o banco rejeitaria a criação da tabela.
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos User
@Table(name = "`user`")
public class User implements Serializable {

    /**
     * Identificador único do usuário.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos User, evitando comparações incorretas baseadas em outros atributos mutáveis.
     * {@code @JdbcTypeCode(SqlTypes.BINARY)} — instrui o Hibernate a tratar o UUID como bytes binários no JDBC, compatível com a definição BINARY(16) no banco.
     * {@code @Column(columnDefinition = "BINARY(16)")} — armazena o UUID como 16 bytes no banco em vez de uma string de 36 caracteres, economizando espaço e melhorando performance em índices.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    /** Telefone opcional do usuário — pode ser nulo quando não informado no cadastro. */
    @Column(name = "phone", length = 20)
    private String phone;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    /**
     * Senha do usuário armazenada neste campo.
     * Certifique-se de que a camada de serviço aplique hash antes de persistir —
     * esta entidade não realiza hashing automaticamente.
     */
    @Column(nullable = false)
    private String password;

    /** Preenchida automaticamente pelo Hibernate no momento em que o registro é inserido no banco. */
    @CreationTimestamp
    @Column(columnDefinition = "datetime", name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
    @UpdateTimestamp
    private OffsetDateTime updateDate;

    /**
     * Grupos de permissão aos quais este usuário pertence.
     * Um usuário pode pertencer a vários grupos e um grupo pode conter vários usuários.
     * As permissões efetivas do usuário são derivadas da união das permissões de todos os seus grupos.
     * Inicializado como {@code HashSet} para garantir que o mesmo grupo não seja associado mais de uma vez.
     */
    @ManyToMany
    @JoinTable(
            name = "user_group",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups = new HashSet<>();

    @ManyToMany(mappedBy = "responsibleUsers")
    private Set<Restaurant> userRestaurants = new HashSet<>();

    /**
     * Associa um grupo de permissão a este usuário.
     *
     * @param group grupo a ser adicionado
     * @return {@code true} se foi adicionado; {@code false} se o usuário já pertencia ao grupo
     */
    public boolean associate(Group group) {
        return this.groups.add(group);
    }

    /**
     * Remove a associação de um grupo de permissão deste usuário.
     *
     * @param group grupo a ser removido
     * @return {@code true} se foi removido; {@code false} se o usuário não pertencia ao grupo
     */
    public boolean dissociate(Group group) {
        return this.groups.remove(group);
    }
}