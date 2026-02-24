package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.domain.exception.EntityInUseException;
import com.pendezzapizza.pendezzapizza_api.domain.exception.OrderNotFoundException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import com.pendezzapizza.pendezzapizza_api.domain.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private  final RestaurantService restaurantService;
    private  final UserService userService;
    private  final PaymentMethodService paymentMethodService;
    private  final OrderItemService orderItemService;

    public List<Order> findAll () {
            return orderRepository.findAll();
        }
    public Page<Order> findAll (Specification<Order> specification , Pageable pageable) {
            return orderRepository.findAll(specification , pageable);
        }

    public Order findById(UUID id) {
            return orderRepository.findByIdOrThrowException(id);
            }
    public Order findByIdMapperSolver (UUID id) {
            return orderRepository.findByIdMapperResolved(id).orElseThrow(
                    () -> new OrderNotFoundException(id));
            }

    @Transactional
    public Order save(Order order) {
            orderRepository.saveAndFlush(order);
            return findByIdMapperSolver(order.getId()) ;
            }

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