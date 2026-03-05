package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.core.cache.cacheannotations.OrdersCacheEvict;
import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.OrderNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;

    @Cacheable("orders")
    public Page<Order> findAll (Specification<Order> specification , Pageable pageable) {
            return orderRepository.findAll(specification , pageable);
        }

    @Cacheable("ordersLastUpdate")
    public OffsetDateTime getLastUpdateDate() {
        return orderRepository.getLastUpdateDate();
    }

    @Cacheable(value = "ordersLastUpdateById" , key = "#orderId")
    public OffsetDateTime getLastUpdateDateById(UUID orderId) {
        return orderRepository.getLastUpdateDateById(orderId );
    }
    @Cacheable(value = "order", key = "#orderId")
    public Order findById(UUID id) {
        return orderRepository.findByIdOrThrowException(id);
    }

    public Order findByIdMapperSolver (UUID id) {
        return orderRepository.findByIdMapperResolved(id).orElseThrow(
            () -> new OrderNotFoundException(id));
    }

    @OrdersCacheEvict
    @Transactional
    public Order save(Order order) {
        orderRepository.saveAndFlush(order);
        return findByIdMapperSolver(order.getId()) ;
    }

    @OrdersCacheEvict
    @Transactional
    public void remove (UUID id) {
        try {
            orderRepository.delete(findById(id));
            orderRepository.flush();
        }
        catch (DataIntegrityViolationException e) {
            throw new EntityInUseException(id);
        }
    }
}