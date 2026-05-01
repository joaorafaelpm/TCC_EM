package com.pendezzapizza.pendezzapizza_api.domain.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Product;
import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
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
 * Interface criada para fazer a ponte entre banco e entidade de <b>produto</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado  </p>
 */
@Repository
public interface ProductRepository extends CustomJPARepository<Product, UUID> , ProductRepositoryQueries {

//    Pega o produto pelo id do restaurante e produto
    @Query("from Product where restaurant.id = :restaurantId and id = :productId")
    Optional<Product> findById (@Param("restaurantId") UUID restaurantId , @Param("productId") UUID productId);

//    Primeiro resolve o LAZY ou EAGER e depois a gente encontra o produto pelo id
    @EntityGraph(attributePaths = {"restaurant"})
    @Query("SELECT p FROM Product p WHERE p.id = :productId")
    Optional<Product> findByProductIdLazySolver(@Param("productId") UUID productId);

//    Filtra por todos os produtos ativos
    @Query("from Product p where p.active=true")
    Page<Product>findAllActives(Pageable pageable) ;

//    Filtra por todos os produtos ativos e nome
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.name LIKE %:productName%")
    Page<Product> findAllActivesByName(String productName, Pageable pageable);

//    Filtra por todos os produtos por restaurante
    Page<Product> findByRestaurant (Restaurant restaurant , Pageable pageable);

//    Filtra por todos os produtos ativos por restaurante
    @Query("from Product p where p.active=true and p.restaurant = :restaurant")
    Page<Product>findActivesByRestaurant(Restaurant restaurant , Pageable pageable) ;

//    Filtra por todas as fotos de produtos por id de restaurante e produto
    @Query("from ProductPhoto f join f.product p where p.restaurant.id = :restaurantId and " +
            "f.product.id = :productId")
    Optional<ProductPhoto> findProductPhotoById (UUID restaurantId , UUID productId) ;

//    Pega a última data de atualização
    @Query("select max(p.updateDate) from Product p where p.active=true")
    OffsetDateTime getAllLastUpdateDate();

//    Pega a última data de atualização pelo id do restaurante só de produtos ativos
    @Query("select max(p.updateDate) from Product p where p.active=true and p.restaurant.id = :restaurantId")
    OffsetDateTime getLastUpdateDateById(@Param("restaurantId") UUID restaurantId);

//    Pega a última data de atualização pelo id do produto
    @Query("select updateDate from Product where id=:productId")
    OffsetDateTime getLastUpdateDateByProductId(@Param("productId") UUID productId);

//    Pega a última data de atualização pelo id do restaurante de todos os produtos
    @Query("select max(p.updateDate) from Product p where p.restaurant.id = :restaurantId")
    OffsetDateTime getLastUpdateDateByIdGetAll(@Param("restaurantId") UUID restaurantId);
}
