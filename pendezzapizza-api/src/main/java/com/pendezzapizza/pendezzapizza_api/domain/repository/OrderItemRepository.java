package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>item do pedido</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface OrderItemRepository extends CustomJPARepository<OrderItem, UUID> {


}
