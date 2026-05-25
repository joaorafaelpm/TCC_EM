package com.pendezzapizza.pendezzapizza_api.infrastructure.repository;

import com.pendezzapizza.pendezzapizza_api.domain.filter.OrderTimeFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.repository.OrderRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    /**
     * Lista pedidos de um restaurante específico, filtrados opcionalmente por intervalo de datas.
     *
     * <p>A paginação é feita diretamente no banco via {@code LIMIT/OFFSET} (setMaxResults/setFirstResult),
     * evitando carregar todos os registros em memória. Uma COUNT query separada fornece o total
     * necessário para o {@link PageImpl}.</p>
     */
    @Override
    public Page<Order> findAllByRestaurantId(OrderTimeFilter orderTimeFilter, UUID restaurantId, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        // ── Query de dados ──────────────────────────────────────────────
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<Order> root = criteria.from(Order.class);

        List<Predicate> predicates = buildPredicates(builder, root, orderTimeFilter);
        predicates.add(builder.equal(root.get("restaurant").get("id"), restaurantId));

        criteria.where(predicates.toArray(new Predicate[0]));

        List<Order> content = manager.createQuery(criteria)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // ── COUNT query (mesmo filtro, sem paginação) ───────────────────
        long total = countByRestaurantId(orderTimeFilter, restaurantId);

        return new PageImpl<>(content, pageable, total);
    }

    /**
     * Lista pedidos de um cliente específico, filtrados opcionalmente por intervalo de datas.
     *
     * <p>Segue a mesma estratégia de paginação no banco da query por restaurante.</p>
     */
    @Override
    public Page<Order> findAllByCustomerId(OrderTimeFilter orderTimeFilter, UUID customerId, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        // ── Query de dados ──────────────────────────────────────────────
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<Order> root = criteria.from(Order.class);

        List<Predicate> predicates = buildPredicates(builder, root, orderTimeFilter);
        predicates.add(builder.equal(root.get("customer").get("id"), customerId));

        criteria.where(predicates.toArray(new Predicate[0]));

        List<Order> content = manager.createQuery(criteria)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        // ── COUNT query (mesmo filtro, sem paginação) ───────────────────
        long total = countByCustomerId(orderTimeFilter, customerId);

        return new PageImpl<>(content, pageable, total);
    }

    // ── Helpers privados ────────────────────────────────────────────────────

    /**
     * Predicados comuns às duas queries: filtro de intervalo de datas de criação.
     * Centralizado para evitar duplicação e garantir consistência entre data e count queries.
     */
    private List<Predicate> buildPredicates(CriteriaBuilder builder, Root<Order> root, OrderTimeFilter filter) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.getStartCreationDate() != null) {
            predicates.add(builder.greaterThanOrEqualTo(
                    root.get("createdAt"), filter.getStartCreationDate()));
        }
        if (filter.getEndCreationDate() != null) {
            predicates.add(builder.lessThanOrEqualTo(
                    root.get("createdAt"), filter.getEndCreationDate()));
        }
        return predicates;
    }

    private long countByRestaurantId(OrderTimeFilter filter, UUID restaurantId) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Order> root = countCriteria.from(Order.class);

        List<Predicate> predicates = buildPredicates(builder, root, filter);
        predicates.add(builder.equal(root.get("restaurant").get("id"), restaurantId));

        countCriteria.select(builder.count(root)).where(predicates.toArray(new Predicate[0]));
        return manager.createQuery(countCriteria).getSingleResult();
    }

    private long countByCustomerId(OrderTimeFilter filter, UUID customerId) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();
        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Order> root = countCriteria.from(Order.class);

        List<Predicate> predicates = buildPredicates(builder, root, filter);
        predicates.add(builder.equal(root.get("customer").get("id"), customerId));

        countCriteria.select(builder.count(root)).where(predicates.toArray(new Predicate[0]));
        return manager.createQuery(countCriteria).getSingleResult();
    }
}