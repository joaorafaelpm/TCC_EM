package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface criada para fazer a ponte entre banco e entidade de <b>usuário</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface UserRepository extends CustomJPARepository<User, UUID> {

//    Resolvendo os parâmetros da entidade
    @Query("""
    select distinct u 
    from User u
    left join fetch u.groups g
    left join fetch g.permission
    where u.email = :email
    """)
    Optional<User> findByEmail(String email);

//    Resolvendo o LAZY e EAGER fetch de grupos
    @EntityGraph(attributePaths = {"groups"})
    Page<User> findAll (Pageable pageable);

//    Pega a última data de atualização
    @Query("select max(u.updateDate) from User u")
    OffsetDateTime getLastUpdateDate();

//    Pega a última data de atualização por id
    @Query("select max(u.updateDate) from User u where u.id = :userId")
    OffsetDateTime getLastUpdateDateById(UUID userId);

//    Resolvendo todos os objetos de usuário para evitar nullPointerException
    @Query("SELECT u FROM User u left join fetch u.groups g WHERE u.id = :userId")
    Optional<User> findByIdGroupLazy (UUID userId) ;

//    Encontrar todos os restaurantes por id de usuário
    @Query("SELECT r FROM User u JOIN u.userRestaurants r WHERE u.id = :userId")
    Page<Restaurant> findUserRestaurantsByUserId(@Param("userId") UUID userId, Pageable pageable);

}
