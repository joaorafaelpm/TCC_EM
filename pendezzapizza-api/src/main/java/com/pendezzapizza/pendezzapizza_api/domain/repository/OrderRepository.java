package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>pedido</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface OrderRepository extends CustomJPARepository<Order, UUID> ,
        JpaSpecificationExecutor<Order> {

//    Essa query recebe todas os outros parâmetros de objetos junto do pedido, isso simplifica o número de selects para receber essa entidade
//    Também concerta um problema de receber algum dado como "null", já que recebe todos os parâmetros de uma vez
    @Query("""
        SELECT p FROM Order p
        JOIN FETCH p.restaurant r
        JOIN FETCH p.deliveryAddress.city c
        JOIN FETCH c.state
        WHERE p.id = :id
    """)
    Optional<Order> findByIdMapperResolved(UUID id);

//    Substituo a principal para incluir resultado páginado ao mesmo tempo que carrego a entidade de User
    @Override
    @Query("from Order p join fetch p.customer")
    Page<Order> findAll (Pageable pageable);

//    Essa query é feita no arquivo orm.xml, em resources
    boolean isOrderManagedBy (UUID orderId , UUID userId);

    //    Pegar a última data de atualização
    @Query("select max(o.updateDate) from Order o")
    OffsetDateTime getLastUpdateDate();

//    Pegar a última data de atualização por id
    @Query("select max(o.updateDate) from Order o where o.id = :orderId")
    OffsetDateTime getLastUpdateDateById(UUID orderId);

}
