/**
 * @summary     Representa a entidade central de pedido da aplicação, gerenciando seu ciclo de vida
 *              completo — desde a criação até entrega ou cancelamento — com validação de transições de status.
 * @difficulty  High
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.domain.model;

import com.pendezzapizza.pendezzapizza_api.domain.event.ConfirmationOrderEvent;
import com.pendezzapizza.pendezzapizza_api.domain.event.OrderCancellationEvent;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidade JPA que representa um pedido e atua como raiz de agregado do domínio.
 *
 * Estende {@code AbstractAggregateRoot} para suportar o padrão de Domain Events —
 * ao confirmar ou cancelar um pedido, eventos de domínio são registrados via
 * {@code registerEvent()} e publicados automaticamente pelo Spring após a transação ser commitada.
 *
 * A tabela é nomeada explicitamente como {@code `order`} pois "order" é uma
 * palavra reservada em SQL — sem as aspas, o banco rejeitaria a criação da tabela.
 *
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false) // Garante que apenas o campo "id" seja usado nas comparações; callSuper=false evita conflito com AbstractAggregateRoot
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`order`")
public class Order extends AbstractAggregateRoot<Order> implements Serializable {

    /**
     * Identificador único do pedido.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos Order, evitando comparações incorretas baseadas em outros atributos mutáveis.
     * {@code @JdbcTypeCode(SqlTypes.BINARY)} — instrui o Hibernate a tratar o UUID como bytes binários no JDBC, compatível com a definição BINARY(16) no banco.
     * {@code @Column(columnDefinition = "BINARY(16)")} — armazena o UUID como 16 bytes no banco em vez de uma string de 36 caracteres, economizando espaço e melhorando performance em índices.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private BigDecimal subtotal;
    private BigDecimal shippingFee;
    private BigDecimal totalCost;

    /** Preenchida automaticamente pelo Hibernate no momento em que o registro é inserido no banco. */
    @CreationTimestamp
    private OffsetDateTime creationDate;

    /** Preenchida pelo domínio no momento em que o pedido é confirmado via {@code confirm()}. */
    private OffsetDateTime confirmationDate;

    /** Preenchida pelo domínio no momento em que o pedido é cancelado via {@code cancel()}. */
    private OffsetDateTime cancellationDate;

    /** Preenchida pelo domínio no momento em que o pedido é marcado como entregue via {@code deliver()}. */
    private OffsetDateTime deliveryDate;

    /**
     * Status atual do pedido, persistido como texto no banco para legibilidade e
     * resiliência a reordenações no enum. Inicializado como CREATED ao criar um novo pedido.
     * As transições válidas entre status são controladas pelo próprio enum OrderStatus.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED;

    /**
     * Forma de pagamento escolhida para este pedido.
     * Carregada com {@code LAZY} pois raramente é necessária fora do contexto de
     * confirmação de pagamento, evitando joins desnecessários na maioria das consultas.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    /** Usuário que realizou o pedido, referenciado pela coluna {@code customer_user_id}. */
    @ManyToOne
    @JoinColumn(nullable = false, name = "customer_user_id")
    private User customer;

    /**
     * Endereço de entrega embutido diretamente nas colunas da tabela de pedido,
     * sem criar uma tabela separada para o endereço.
     */
    @Embedded
    private Address deliveryAddress;

    /**
     * Itens que compõem este pedido.
     * {@code CascadeType.ALL} garante que operações de persistência (salvar, deletar)
     * no pedido sejam propagadas automaticamente para os itens, sem necessidade de gerenciá-los separadamente.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();

    /** Preenchida automaticamente pelo Hibernate sempre que o registro for atualizado no banco. */
    @UpdateTimestamp
    private OffsetDateTime updateDate;

    /**
     * Recalcula o subtotal e o custo total do pedido com base nos itens presentes.
     * Deve ser chamado sempre que itens forem adicionados ou removidos do pedido,
     * pois os valores não são atualizados automaticamente.
     */
    public void calculateTotalOrderCost() {
        getItems().forEach(OrderItem::calculateTotalPrice);

        this.subtotal = getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalCost = this.subtotal.add(this.shippingFee);
    }

    /**
     * Confirma o pedido, registrando o evento de confirmação para processamento assíncrono.
     * A data de confirmação é capturada no momento exato da chamada.
     *
     * @throws BusinessException se a transição do status atual para CONFIRMED não for permitida
     */
    public void confirm() {
        setOrderStatus(OrderStatus.CONFIRMED);
        setConfirmationDate(OffsetDateTime.now());

        // Publica o evento após a validação de status para garantir que só dispara em transições válidas
        registerEvent(new ConfirmationOrderEvent(this));
    }

    /**
     * Marca o pedido como entregue e registra o momento da entrega.
     *
     * @throws BusinessException se a transição do status atual para DELIVERED não for permitida
     */
    public void deliver() {
        setOrderStatus(OrderStatus.DELIVERED);
        setDeliveryDate(OffsetDateTime.now());
    }

    /**
     * Cancela o pedido, registrando o evento de cancelamento para processamento assíncrono.
     * A data de cancelamento é capturada no momento exato da chamada.
     *
     * @throws BusinessException se a transição do status atual para CANCELED não for permitida
     */
    public void cancel() {
        setOrderStatus(OrderStatus.CANCELED);
        setCancellationDate(OffsetDateTime.now());

        // Publica o evento após a validação de status para garantir que só dispara em transições válidas
        registerEvent(new OrderCancellationEvent(this));
    }

    /**
     * Valida e aplica uma nova transição de status ao pedido.
     * A lógica de quais transições são permitidas está encapsulada no enum OrderStatus,
     * mantendo as regras de negócio centralizadas e fora desta entidade.
     *
     * @param newStatus novo status a ser aplicado ao pedido
     * @throws BusinessException se a transição do status atual para {@code newStatus} não for permitida
     */
    private void setOrderStatus(OrderStatus newStatus) {
        if (getOrderStatus().cannotChangeTo(newStatus)) {
            throw new BusinessException(String.format(
                    "Status do pedido '%s' não pode ser alterado de '%s' para '%s'",
                    getId(), getOrderStatus().getDescription(), newStatus.getDescription()
            ));
        }

        this.orderStatus = newStatus;
    }
}