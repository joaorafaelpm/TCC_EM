/**
 * @summary     Representa a entidade central de restaurante, gerenciando seu ciclo de vida
 *              completo — ativação, abertura, produtos, formas de pagamento e usuários responsáveis.
 * @difficulty  High
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
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Entidade JPA que representa um restaurante cadastrado na plataforma.
 * Centraliza as regras de negócio relacionadas ao estado operacional do restaurante
 * (ativo/inativo, aberto/fechado) e suas associações com produtos, pagamentos e responsáveis.
 *
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos Restaurant
public class Restaurant implements Serializable {

    /**
     * Identificador único do restaurante.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos Restaurant, evitando comparações incorretas baseadas em outros atributos mutáveis.
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

    @Column(name = "shipping_fee", nullable = false)
    private BigDecimal shippingFee;

    /**
     * Endereço do restaurante embutido diretamente nas colunas desta tabela,
     * sem criar uma tabela separada para o endereço.
     */
    @Embedded
    private Address address;

    /**
     * Indica se o restaurante está cadastrado e habilitado na plataforma.
     * Um restaurante inativo não pode ser aberto — verifique {@code canOpen()} antes de abrir.
     * Inicializado como {@code TRUE} para que restaurantes recém-cadastrados já estejam ativos por padrão.
     */
    private Boolean active = Boolean.TRUE;

    /**
     * Indica se o restaurante está aceitando pedidos no momento.
     * Independente do status de ativação — um restaurante pode estar ativo mas fechado.
     * Inicializado como {@code FALSE} pois um restaurante recém-cadastrado não deve receber pedidos automaticamente.
     */
    private Boolean open = Boolean.TRUE;

    /** Preenchida automaticamente pelo Hibernate no momento em que o registro é inserido no banco. */
    @CreationTimestamp
    @Column(columnDefinition = "datetime", name = "registration_date")
    private OffsetDateTime registrationDate;

    /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
    @UpdateTimestamp
    @Column(columnDefinition = "datetime", name = "update_date", nullable = false)
    private OffsetDateTime updateDate;

    /**
     * Lista de produtos do cardápio deste restaurante.
     * {@code CascadeType.ALL} garante que operações de persistência (salvar, deletar)
     * no restaurante sejam propagadas automaticamente para os produtos, sem necessidade de gerenciá-los separadamente.
     * Carregada com {@code LAZY} para evitar carregar todo o cardápio em consultas que não precisam dos produtos.
     */
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

//  =========== Campos adicionais para exibição na interface do cliente ===========
    /** Texto de apresentação do restaurante exibido ao cliente na listagem e página de detalhes. */
    @Column(name = "description", length = 500)
    private String description;

    /** Tempo médio estimado de entrega em minutos, exibido ao cliente antes de realizar o pedido. */
    @Column(name = "average_delivery_time_minutes")
    private Integer averageDeliveryTimeMinutes;

    /** Valor mínimo exigido para que um pedido seja aceito por este restaurante. */
    @Column(name = "minimum_order_value")
    private BigDecimal minimumOrderValue;
//  =================================================================================

    /**
     * Formas de pagamento aceitas por este restaurante.
     * Um restaurante pode aceitar vários métodos e um mesmo método pode ser aceito por vários restaurantes.
     * Inicializado como {@code HashSet} para garantir que não haja métodos de pagamento duplicados no conjunto.
     */
    @ManyToMany
    @JoinTable(
            name = "restaurant_payment_method",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "payment_method_id")
    )
    private Set<PaymentMethod> paymentMethods = new HashSet<>();

    /**
     * Usuários responsáveis pela gestão deste restaurante.
     * Um restaurante pode ter vários responsáveis e um usuário pode gerenciar vários restaurantes.
     * Inicializado como {@code HashSet} para garantir que o mesmo usuário não seja adicionado mais de uma vez.
     */
    @ManyToMany
    @JoinTable(
            name = "restaurant_user_responsible",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> responsibleUsers = new HashSet<>();

    /**
     * Ativa o restaurante, tornando-o habilitado na plataforma.
     * Prefira verificar {@code canActivate()} antes de chamar este método para evitar ativações redundantes.
     */
    public void activate() { this.active = true; }

    /**
     * Desativa o restaurante, removendo-o da plataforma.
     * Prefira verificar {@code canDeactivate()} antes de chamar este método para evitar desativações redundantes.
     */
    public void deactivate() { this.active = false; }

    /**
     * Abre o restaurante para recebimento de pedidos.
     * Prefira verificar {@code canOpen()} antes de chamar este método, pois restaurantes
     * inativos não devem ser abertos.
     */
    public void open() { this.open = true; }

    /**
     * Fecha o restaurante, impedindo o recebimento de novos pedidos.
     * Prefira verificar {@code canClose()} antes de chamar este método para evitar fechamentos redundantes.
     */
    public void close() { this.open = false; }

    /**
     * Associa uma forma de pagamento a este restaurante.
     *
     * @param method forma de pagamento a ser adicionada
     * @return {@code true} se foi adicionada; {@code false} se já estava associada
     */
    public boolean associatePaymentMethod(PaymentMethod method) {
        return this.paymentMethods.add(method);
    }

    /**
     * Remove a associação de uma forma de pagamento deste restaurante.
     *
     * @param method forma de pagamento a ser removida
     * @return {@code true} se foi removida; {@code false} se não estava associada
     */
    public boolean disassociatePaymentMethod(PaymentMethod method) {
        return this.paymentMethods.remove(method);
    }

    /**
     * Associa um usuário como responsável por este restaurante.
     *
     * @param user usuário a ser adicionado como responsável
     * @return {@code true} se foi adicionado; {@code false} se já era responsável
     */
    public boolean associateResponsibleUser(User user) {
        boolean added = this.responsibleUsers.add(user);
        if (added) {
            user.getUserRestaurants().add(this); // sincroniza o lado inverso
        }
        return added;
    }

    /**
     * Remove a associação de um usuário responsável deste restaurante.
     *
     * @param user usuário a ser removido da lista de responsáveis
     * @return {@code true} se foi removido; {@code false} se não era responsável
     */
    public boolean disassociateResponsibleUser(User user) {
        boolean removed = this.responsibleUsers.remove(user);
        if (removed) {
            user.getUserRestaurants().remove(this); // sincroniza o lado inverso
        }
        return removed;
    }
    /**
     * Adiciona um produto ao cardápio deste restaurante.
     *
     * @param product produto a ser adicionado
     * @return {@code true} se foi adicionado com sucesso
     */
    public boolean addProduct(Product product) {
        return this.products.add(product);
    }

    /**
     * Remove um produto do cardápio deste restaurante.
     *
     * @param product produto a ser removido
     * @return {@code true} se foi removido; {@code false} se não estava no cardápio
     */
    public boolean removeProduct(Product product) {
        return this.products.remove(product);
    }

    /**
     * Verifica se este restaurante aceita uma determinada forma de pagamento.
     * Útil para validar pedidos antes de processá-los.
     *
     * @param method forma de pagamento a ser verificada
     * @return {@code true} se o restaurante aceita o método informado
     */
    public boolean acceptsPaymentMethod(PaymentMethod method) {
        return this.paymentMethods.contains(method);
    }

    /**
     * Verifica se este restaurante não aceita uma determinada forma de pagamento.
     * Conveniente para lançar exceções de validação sem inverter a lógica no chamador.
     *
     * @param method forma de pagamento a ser verificada
     * @return {@code true} se o restaurante não aceita o método informado
     */
    public boolean doesNotAcceptPaymentMethod(PaymentMethod method) {
        return !acceptsPaymentMethod(method);
    }

    /**
     * Verifica se o restaurante está habilitado na plataforma.
     *
     * @return {@code true} se o restaurante estiver ativo
     */
    public boolean isActive() { return this.active; }

    /**
     * Verifica se o restaurante está desabilitado na plataforma.
     *
     * @return {@code true} se o restaurante estiver inativo
     */
    public boolean isInactive() { return !isActive(); }

    /**
     * Verifica se o restaurante está aceitando pedidos no momento.
     *
     * @return {@code true} se o restaurante estiver aberto
     */
    public boolean isOpen() { return this.open; }

    /**
     * Verifica se o restaurante não está aceitando pedidos no momento.
     *
     * @return {@code true} se o restaurante estiver fechado
     */
    public boolean isClosed() { return !isOpen(); }

    /**
     * Indica se o restaurante pode ser aberto para receber pedidos.
     * Requer que o restaurante esteja simultaneamente ativo e fechado —
     * restaurantes inativos não podem ser abertos independentemente do estado de abertura.
     *
     * @return {@code true} se a abertura for permitida
     */
    public boolean canOpen() {
        return isActive() && isClosed();
    }

    /**
     * Indica se o restaurante pode ser fechado.
     *
     * @return {@code true} se o restaurante estiver aberto e puder ser fechado
     */
    public boolean canClose() { return isOpen(); }

    /**
     * Indica se o restaurante pode ser ativado, ou seja, se atualmente está inativo.
     *
     * @return {@code true} se a ativação for permitida
     */
    public boolean canActivate() { return isInactive(); }

    /**
     * Indica se o restaurante pode ser desativado, ou seja, se atualmente está ativo.
     *
     * @return {@code true} se a desativação for permitida
     */
    public boolean canDeactivate() { return isActive(); }
}