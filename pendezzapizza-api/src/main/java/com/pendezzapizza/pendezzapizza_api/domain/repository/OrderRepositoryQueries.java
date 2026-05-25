package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.filter.OrderTimeFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Queries customizadas para a entidade {@link Order}.
 *
 * <p>Separadas em dois métodos com semântica clara:
 * um para listagem pelo restaurante (uso do gestor/admin)
 * e outro para listagem pelo cliente (uso do próprio usuário).</p>
 */
public interface OrderRepositoryQueries {

    /**
     * Retorna pedidos de um restaurante específico.
     * Destinado a gestores do restaurante ou admins.
     */
    Page<Order> findAllByRestaurantId(OrderTimeFilter orderTimeFilter, UUID restaurantId, Pageable pageable);

    /**
     * Retorna pedidos de um cliente específico.
     * Destinado ao próprio usuário autenticado.
     *
     * @param customerId ID do cliente — deve ser validado no controller contra o JWT
     */
    Page<Order> findAllByCustomerId(OrderTimeFilter orderTimeFilter, UUID customerId, Pageable pageable);
}