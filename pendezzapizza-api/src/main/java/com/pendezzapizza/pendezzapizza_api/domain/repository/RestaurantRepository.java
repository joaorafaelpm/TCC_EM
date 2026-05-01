package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.PaymentMethod;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>restaurante</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface RestaurantRepository
        extends CustomJPARepository<Restaurant , UUID> , RestaurantRepositoryQueries,
        JpaSpecificationExecutor<Restaurant> {

//    Resolvendo problemas de fetch LAZY e EAGER de formas de pagamento
    @EntityGraph(attributePaths = "paymentMethods")
    Page<Restaurant> findAll (Pageable pageable);

//    Encontra o restaurante pelo id da foto
    @Query("from RestaurantPhoto rp join rp.restaurant r where r.id = :restaurantId")
    Optional<RestaurantPhoto> findRestaurantPhotoById (UUID restaurantId) ;

//    Recebo todos os objetos de uma vez para evitar nullPointerException
    @Query("""
    SELECT r FROM Restaurant r
    JOIN FETCH r.address.city c
    JOIN FETCH c.state
    WHERE r.id = :id
""")
    Optional<Restaurant> findByIdMapperResolved(UUID id);

//    Implementação no orm.xml nos resources
    boolean existsResponsible(UUID restaurantId , UUID userId);

//    Última data de atualização
    @Query("select max(r.updateDate) from Restaurant r")
    OffsetDateTime getLastUpdateDate();

//    Última data de atualização por id
    @Query("select max(r.updateDate) from Restaurant r where r.id = :restaurantId")
    OffsetDateTime getLastUpdateDateById(UUID restaurantId);

//    Encontrar todos os usuários por id de restaurante
    @Query("SELECT u FROM Restaurant r JOIN r.responsibleUsers u WHERE r.id = :restaurantId")
    Page<User> findResponsibleUsersByRestaurantId(@Param("restaurantId") UUID restaurantId, Pageable pageable);

//    Encontrar todas as formas de pagamento por id de restaurante
    @Query("SELECT u FROM Restaurant r JOIN r.paymentMethods u WHERE r.id = :restaurantId")
    Page<PaymentMethod> findPaymentMethodsByRestaurantId(@Param("restaurantId") UUID restaurantId, Pageable pageable);

}
