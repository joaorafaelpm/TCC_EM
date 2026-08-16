package com.pendezzapizza.pendezzapizza_api.infrastructure.service.query;

import com.pendezzapizza.pendezzapizza_api.domain.filter.DailySalesFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.model.dto.*;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.OrderStatus;
import com.pendezzapizza.pendezzapizza_api.domain.model.enums.SaleIncludeField;
import com.pendezzapizza.pendezzapizza_api.domain.service.SaleQueryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class SaleQueryServiceImpl implements SaleQueryService {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<DailySale> viewDailySales(DailySalesFilter filter, String timeOffSet) {
        var builder = manager.getCriteriaBuilder();
        var query = builder.createQuery(DailySale.class);
        var root = query.from(Order.class);
        var predicates = new ArrayList<Predicate>();

        var functionConvertTzCreationDate = builder.function(
                "convert_tz", Date.class, root.get("creationDate"),
                builder.literal("+00:00"), builder.literal(timeOffSet));

        var functionDateCreationDate = builder.function(
                "date", Date.class, functionConvertTzCreationDate);

        var selection = builder.construct(DailySale.class,
                functionDateCreationDate,
                builder.count(root.get("id")),
                builder.sum(root.get("totalCost")));

        if (filter.getRestaurantId() != null) {
            predicates.add(builder.equal(root.get("restaurant").get("id"), filter.getRestaurantId()));
        }
        if (filter.getStartCreationDate() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("creationDate"), filter.getStartCreationDate()));
        }
        if (filter.getEndCreationDate() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("creationDate"), filter.getEndCreationDate()));
        }

        predicates.add(root.get("orderStatus").in(OrderStatus.DELIVERED, OrderStatus.CONFIRMED));

        query.select(selection);
        query.where(predicates.toArray(new Predicate[0]));
        query.groupBy(functionDateCreationDate);

        return manager.createQuery(query).getResultList();
    }

    @Override
    public EnrichedDailySale viewEnrichedDailySales(
            DailySalesFilter filter,
            String timeOffset,
            Set<SaleIncludeField> include) {

        List<DailySale> baseSales = viewDailySales(filter, timeOffset);

        if (baseSales.isEmpty()) {
            return new EnrichedDailySale(null, null, 0L, BigDecimal.ZERO);
        }

        Date startDate     = baseSales.getFirst().getDate();
        Date endDate       = baseSales.getLast().getDate();
        Long totalSales    = baseSales.stream().mapToLong(DailySale::getTotalSales).sum();
        BigDecimal totalBilled = baseSales.stream()
                .map(DailySale::getTotalBilled)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Date peakDay = baseSales.stream()
                .max(Comparator.comparingLong(DailySale::getTotalSales))
                .map(DailySale::getDate)
                .orElse(null);

        EnrichedDailySale result = new EnrichedDailySale(startDate, endDate, totalSales, totalBilled);
        result.setPeakDay(peakDay);

        if (include.contains(SaleIncludeField.PRODUCTS)) {
            List<DailyProductSummary> products = queryProductSummaries(filter, timeOffset);
            result.setProducts(products);
            result.setHighlights(new EnrichedDailySaleHighlights(
                    products.isEmpty() ? null : products.getFirst(),
                    null));
        }

        if (include.contains(SaleIncludeField.CUSTOMERS)) {
            List<DailyCustomerSummary> customers = queryCustomerSummaries(filter, timeOffset);
            result.setCustomers(customers);

            DailyCustomerSummary topCustomer = customers.isEmpty() ? null : customers.getFirst();

            if (result.getHighlights() == null) {
                result.setHighlights(new EnrichedDailySaleHighlights(null, topCustomer));
            } else {
                result.getHighlights().setTopCustomer(topCustomer);
            }
        }

        if (include.contains(SaleIncludeField.PRODUCTS) || include.contains(SaleIncludeField.CUSTOMERS)) {
            result.setDailyBreakdown(buildDailyBreakdown(filter, timeOffset, baseSales, include));
        }

        return result;
    }

    private List<DailyBreakdown> buildDailyBreakdown(
            DailySalesFilter filter,
            String timeOffset,
            List<DailySale> baseSales,
            Set<SaleIncludeField> include) {

        Map<Date, List<DailyProductSummary>> productsByDay =
                include.contains(SaleIncludeField.PRODUCTS)
                        ? queryProductSummariesByDay(filter, timeOffset)
                        : Map.of();

        Map<Date, List<DailyCustomerSummary>> customersByDay =
                include.contains(SaleIncludeField.CUSTOMERS)
                        ? queryCustomerSummariesByDay(filter, timeOffset)
                        : Map.of();

        return baseSales.stream().map(sale -> {
            DailyBreakdown breakdown = new DailyBreakdown(
                    sale.getDate(), sale.getTotalSales(), sale.getTotalBilled());
            breakdown.setProducts(productsByDay.getOrDefault(sale.getDate(), List.of()));
            breakdown.setCustomers(customersByDay.getOrDefault(sale.getDate(), List.of()));
            return breakdown;
        }).collect(Collectors.toList());
    }

    // ── products by day ───────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private Map<Date, List<DailyProductSummary>> queryProductSummariesByDay(
            DailySalesFilter filter, String timeOffset) {

        var sql = new StringBuilder("""
                SELECT DATE(CONVERT_TZ(o.creation_date, '+00:00', :timeOffset)) AS day,
                       BIN_TO_UUID(p.id)  AS productId,
                       p.name             AS productName,
                       SUM(oi.quantity)   AS totalQuantity
                FROM order_item oi
                INNER JOIN `order`  o ON o.id = oi.order_id
                INNER JOIN product  p ON p.id = oi.product_id
                WHERE o.order_status IN ('DELIVERED', 'CONFIRMED')
                """);

        appendDateAndRestaurantFilters(sql, filter);
        sql.append("GROUP BY day, p.id, p.name ORDER BY day, SUM(oi.quantity) DESC");

        var query = manager.createNativeQuery(sql.toString())
                .setParameter("timeOffset", timeOffset);
        applyDateAndRestaurantParams(query, filter);

        Map<Date, List<DailyProductSummary>> result = new LinkedHashMap<>();
        for (Object[] row : (List<Object[]>) query.getResultList()) {
            Date day = (Date) row[0];
            result.computeIfAbsent(day, k -> new ArrayList<>())
                    .add(new DailyProductSummary(
                            (String) row[1],
                            (String) row[2],
                            ((Number) row[3]).longValue()));
        }
        return result;
    }

    // ── customers by day ──────────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private Map<Date, List<DailyCustomerSummary>> queryCustomerSummariesByDay(
            DailySalesFilter filter, String timeOffset) {

        var sql = new StringBuilder("""
                SELECT DATE(CONVERT_TZ(o.creation_date, '+00:00', :timeOffset)) AS day,
                       BIN_TO_UUID(o.customer_user_id)                          AS customerId,
                       u.name                                                    AS customerName,
                       COUNT(o.id)                                              AS totalOrders
                FROM `order` o
                INNER JOIN `user` u ON u.id = o.customer_user_id
                WHERE o.order_status IN ('DELIVERED', 'CONFIRMED')
                """);

        appendDateAndRestaurantFilters(sql, filter);
        sql.append("GROUP BY day, o.customer_user_id, u.name ORDER BY day, COUNT(o.id) DESC");

        var query = manager.createNativeQuery(sql.toString())
                .setParameter("timeOffset", timeOffset);
        applyDateAndRestaurantParams(query, filter);

        Map<Date, List<DailyCustomerSummary>> result = new LinkedHashMap<>();
        for (Object[] row : (List<Object[]>) query.getResultList()) {
            Date day = (Date) row[0];
            result.computeIfAbsent(day, k -> new ArrayList<>())
                    .add(new DailyCustomerSummary(
                            UUID.fromString((String) row[1]),
                            (String) row[2],
                            ((Number) row[3]).longValue()));
        }
        return result;
    }

    // ── products aggregated ───────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private List<DailyProductSummary> queryProductSummaries(
            DailySalesFilter filter, String timeOffset) {

        var sql = new StringBuilder("""
                SELECT BIN_TO_UUID(p.id) AS productId,
                       p.name            AS productName,
                       SUM(oi.quantity)  AS totalQuantity
                FROM order_item oi
                INNER JOIN `order` o ON o.id = oi.order_id
                INNER JOIN product  p ON p.id = oi.product_id
                WHERE o.order_status IN ('DELIVERED', 'CONFIRMED')
                """);

        appendDateAndRestaurantFilters(sql, filter);
        sql.append("GROUP BY p.id, p.name ORDER BY SUM(oi.quantity) DESC");

        var query = manager.createNativeQuery(sql.toString());
        applyDateAndRestaurantParams(query, filter);

        return ((List<Object[]>) query.getResultList()).stream()
                .map(row -> new DailyProductSummary(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).longValue()))
                .collect(Collectors.toList());
    }

    // ── customers aggregated ──────────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private List<DailyCustomerSummary> queryCustomerSummaries(
            DailySalesFilter filter, String timeOffset) {

        var sql = new StringBuilder("""
                SELECT BIN_TO_UUID(o.customer_user_id) AS customerId,
                       u.name                           AS customerName,
                       COUNT(o.id)                      AS totalOrders
                FROM `order` o
                INNER JOIN `user` u ON u.id = o.customer_user_id
                WHERE o.order_status IN ('DELIVERED', 'CONFIRMED')
                """);

        appendDateAndRestaurantFilters(sql, filter);
        sql.append("GROUP BY o.customer_user_id, u.name ORDER BY COUNT(o.id) DESC");

        var query = manager.createNativeQuery(sql.toString());
        applyDateAndRestaurantParams(query, filter);

        return ((List<Object[]>) query.getResultList()).stream()
                .map(row -> new DailyCustomerSummary(
                        UUID.fromString((String) row[0]),
                        (String) row[1],
                        ((Number) row[2]).longValue()))
                .collect(Collectors.toList());
    }

    // ── filter helpers ────────────────────────────────────────────────────────

    private void appendDateAndRestaurantFilters(StringBuilder sql, DailySalesFilter filter) {
        if (filter.getRestaurantId() != null)      sql.append("AND o.restaurant_id = UUID_TO_BIN(:restaurantId) ");
        if (filter.getStartCreationDate() != null) sql.append("AND o.creation_date >= :startDate ");
        if (filter.getEndCreationDate() != null)   sql.append("AND o.creation_date <= :endDate ");
    }

    private void applyDateAndRestaurantParams(jakarta.persistence.Query query, DailySalesFilter filter) {
        if (filter.getRestaurantId() != null)      query.setParameter("restaurantId", filter.getRestaurantId().toString());
        if (filter.getStartCreationDate() != null) query.setParameter("startDate", filter.getStartCreationDate());
        if (filter.getEndCreationDate() != null)   query.setParameter("endDate", filter.getEndCreationDate());
    }
}