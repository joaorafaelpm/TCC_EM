package com.pendezzapizza.pendezzapizza_api.infrastructure.repository;

import com.pendezzapizza.pendezzapizza_api.domain.exception.*;
import com.pendezzapizza.pendezzapizza_api.domain.model.*;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CustomJPARepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.Optional;
import java.util.UUID;

public class CustomJPARepositoryImpl<T , ID>
        extends SimpleJpaRepository<T , ID>
        implements CustomJPARepository<T , ID> {

    private EntityManager manager ;

    public CustomJPARepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                                   EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.manager = entityManager;
    }

    @Override
    public void detach(T entity) {
        manager.detach(entity);
    }

    @Override
    public Optional<T> findFirst () {
        var jpql = "from " + getDomainClass().getName();

        T entity = manager.createQuery(jpql , getDomainClass())
                .setMaxResults(1)
                .getSingleResult();

        return Optional.ofNullable(entity) ;
    }

    @Override
    public T findByIdOrThrowException(UUID id) {
        try {
            String jpql = "FROM " + getDomainClass().getSimpleName() + " e WHERE e.id = :id";

            return manager.createQuery(jpql, getDomainClass())
                    .setParameter("id", id)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw createSpecificNotFoundException(id);
        }
    }
    @Override
    public T findByIdOrThrowException(
            Object foreignEntity,
            UUID foreignId ,
            UUID entityId
    ) {
        try {
            String jpql =
                    "FROM " + getDomainClass().getSimpleName()
                            + " e WHERE e." + foreignEntity.getClass().getSimpleName().toLowerCase() + ".id = :foreignId"
                            + " AND e.id = :entityId";

            return manager.createQuery(jpql, getDomainClass())
                    .setParameter("foreignId", foreignId)
                    .setParameter("entityId", entityId)
                    .getSingleResult();

        } catch (NoResultException e) {
            throw createSpecificNotFoundException(foreignEntity , entityId , foreignId);
        }
    }


    private EntityNotFoundException createSpecificNotFoundException(UUID id) {
        Class<?> model = getDomainClass();
        if (model.equals(City.class)) {
            return new CityNotFoundException(id);
        }
        if (model.equals(State.class)) {
            return new StateNotFoundException(id);
        }
        if (model.equals(Group.class)) {
            return new GroupNotFoundException(id);
        }
        if (model.equals(Permission.class)) {
            return new PermissionNotFoundException(id);
        }
        if (model.equals(OrderItem.class)) {
            return new OrderItemNotFoundException(id);
        }
        if (model.equals(Order.class)) {
            return new OrderNotFoundException(id);
        }
        if (model.equals(PaymentMethod.class)) {
            return new PaymentMethodNotFoundException(id);
        }
        if (model.equals(Restaurant.class)) {
            return new RestaurantNotFoundException(id);
        }

        // fallback caso alguma entidade nova seja criada sem exception específica
        return new EntityNotFoundException(
                model.getSimpleName() + " com ID " + id + " não foi encontrado."
        );
    }
    private EntityNotFoundException createSpecificNotFoundException(Object o , UUID entityId , UUID foreignId) {
        Class<?> model = getDomainClass();

        if (model.equals(Product.class)) {
            return new ProductNotFoundException(entityId , foreignId);
        }
        if (model.equals(ProductPhoto.class)) {
            return new ProductPhotoNotFoundException(entityId , foreignId);
        }

        // fallback caso alguma entidade nova seja criada sem exception específica
        return new EntityNotFoundException(
                model.getSimpleName() + " com ID " + entityId +
                        " não foi encontrado no " + o.getClass().getSimpleName() +
                        " com o id de " + foreignId
        );
    }






}
