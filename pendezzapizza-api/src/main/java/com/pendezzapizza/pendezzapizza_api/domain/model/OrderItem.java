/**
 * @summary     Representa um item individual dentro de um pedido, associando um produto
 *              a uma quantidade, preço unitário e observação opcional do cliente.
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
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidade JPA que representa um item de pedido.
 * Implementa {@code Serializable} para permitir que objetos desta classe
 * sejam convertidos em bytes — necessário para cache e transferência entre sistemas.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Garante que apenas o campo "id" seja usado nas comparações de igualdade entre objetos OrderItem
public class OrderItem implements Serializable {

    /**
     * Identificador único do item do pedido.
     *
     * {@code @Id} — marca este campo como chave primária da tabela.
     * {@code @GeneratedValue} — o valor é gerado automaticamente pelo banco/Hibernate, sem precisar setar manualmente.
     * {@code @EqualsAndHashCode.Include} — inclui apenas este campo ao comparar dois objetos OrderItem, evitando comparações incorretas baseadas em outros atributos mutáveis.
     * {@code @JdbcTypeCode(SqlTypes.BINARY)} — instrui o Hibernate a tratar o UUID como bytes binários no JDBC, compatível com a definição BINARY(16) no banco.
     * {@code @Column(columnDefinition = "BINARY(16)")} — armazena o UUID como 16 bytes no banco em vez de uma string de 36 caracteres, economizando espaço e melhorando performance em índices.
     */
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private Integer quantity;
    private BigDecimal unitPrice;

    /** Valor calculado por {@code calculateTotalPrice()} — não deve ser definido manualmente. */
    private BigDecimal totalPrice;

    /** Observação opcional do cliente para este item (ex: "sem cebola"). Pode ser nulo. */
    private String note;

    /** Pedido ao qual este item pertence. */
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /** Produto referenciado por este item do pedido. */
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Calcula o preço total deste item (quantidade × preço unitário) e atualiza o campo {@code totalPrice}.
     * Trata valores nulos de preço e quantidade como zero para evitar exceções em itens incompletos.
     *
     * @return total calculado, nunca {@code null}
     */
    public BigDecimal calculateTotalPrice() {
        BigDecimal price = this.getUnitPrice();
        Integer qtt = this.getQuantity();

        // Evita NullPointerException ao multiplicar caso o item ainda não tenha preço ou quantidade definidos
        if (price == null) {
            price = BigDecimal.ZERO;
        }

        if (qtt == null) {
            qtt = 0;
        }

        BigDecimal total = price.multiply(BigDecimal.valueOf(qtt));
        setTotalPrice(total);

        return total;
    }
}