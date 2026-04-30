package com.pendezzapizza.pendezzapizza_api.infrastructure.repository;

import com.pendezzapizza.pendezzapizza_api.domain.exception.*;
import com.pendezzapizza.pendezzapizza_api.domain.model.*;
import com.pendezzapizza.pendezzapizza_api.domain.repository.CustomJPARepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.StringUtils;

import java.util.*;

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

//    @Override
//    public Page<T> findByName(String name, Pageable pageable) {
//        CriteriaBuilder builder = manager.getCriteriaBuilder();
//
//        // --- Main query (fetches the data) ---
//        CriteriaQuery<T> criteria = builder.createQuery(getDomainClass());
//        Root<T> root = criteria.from(getDomainClass());
//
//        var predicates = new ArrayList<Predicate>();
//        if (StringUtils.hasLength(name)) {
//            predicates.add(builder.like(
//                    builder.lower(root.get("name")),
//                    "%" + name.toLowerCase() + "%"
//            ));
//        }
//        criteria.where(predicates.toArray(new Predicate[0]));
//
//        // --- Ordenação por relevância ---
//        if (StringUtils.hasLength(name)) {
//            // LOCATE retorna a posição da substring no nome (0 = não encontrou)
//            // Quem tem posição menor (match mais à esquerda) aparece primeiro
//            Expression<Integer> locateExact = builder.locate(
//                    builder.lower(root.get("name")),
//                    name.toLowerCase()
//            );
//            // Resultado começa pelo nome que começa com o termo, depois os que contêm
//            criteria.orderBy(builder.asc(locateExact));
//        }
//
//        TypedQuery<T> query = manager.createQuery(criteria);
//        query.setFirstResult((int) pageable.getOffset());
//        query.setMaxResults(pageable.getPageSize());
//
//        List<T> results = query.getResultList();
//
//        // --- Count query ---
//        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
//        Root<T> countRoot = countCriteria.from(getDomainClass());
//
//        var countPredicates = new ArrayList<Predicate>();
//        if (StringUtils.hasLength(name)) {
//            countPredicates.add(builder.like(
//                    builder.lower(countRoot.get("name")),
//                    "%" + name.toLowerCase() + "%"
//            ));
//        }
//        countCriteria.select(builder.count(countRoot));
//        countCriteria.where(countPredicates.toArray(new Predicate[0]));
//
//        Long total = manager.createQuery(countCriteria).getSingleResult();
//
//        return new PageImpl<>(results, pageable, total);
//    }

    @Override
    public Page<T> findByName(String name, Pageable pageable) {
        CriteriaBuilder builder = manager.getCriteriaBuilder();

        CriteriaQuery<T> criteria = builder.createQuery(getDomainClass());
        Root<T> root = criteria.from(getDomainClass());

        var predicates = new ArrayList<Predicate>();
        if (StringUtils.hasLength(name)) {
            // Mantém o LIKE para filtrar no banco (eficiente)
            // Usamos a primeira e última letra para dar uma pré-filtrada
            predicates.add(builder.like(
                    builder.lower(root.get("name")),
                    "%" + name.toLowerCase().charAt(0) + "%"
            ));
        }
        criteria.where(predicates.toArray(new Predicate[0]));

        // Busca todos os candidatos (sem paginação ainda)
        List<T> candidates = manager.createQuery(criteria).getResultList();

        // Reordena e filtra em memória pelo score fuzzy
        String search = StringUtils.hasLength(name) ? name.toLowerCase() : "";

        List<T> sorted = candidates.stream()
                .map(entity -> {
                    String entityName = getNameFromEntity(entity); // veja implementação abaixo
                    int score = fuzzyScore(entityName.toLowerCase(), search);
                    return Map.entry(entity, score);
                })
                .filter(e -> e.getValue() >= 0)          // -1 = sem match
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .toList();

        // Aplica paginação manualmente
        int total = sorted.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), total);
        List<T> pageContent = (start >= total) ? List.of() : sorted.subList(start, end);

        return new PageImpl<>(pageContent, pageable, total);
    }

    /**
     * Retorna a posição da última letra matched (score).
     * Menor score = match mais compacto = mais relevante.
     * Retorna -1 se não encontrar todas as letras em sequência.
     */
    private int fuzzyScore(String text, String search) {
        if (!StringUtils.hasLength(search)) return 0;

        int searchIndex = 0;
        int lastMatchPos = 0;

        for (int i = 0; i < text.length() && searchIndex < search.length(); i++) {
            if (text.charAt(i) == search.charAt(searchIndex)) {
                lastMatchPos = i;
                searchIndex++;
            }
        }

        // Se não consumiu todas as letras do search, não é match
        if (searchIndex < search.length()) return -1;

        return lastMatchPos;
    }

    /**
     * Extrai o campo "name" da entidade genericamente.
     * Adapte conforme sua estrutura — se tiver uma interface ou classe base, use ela.
     */
    private String getNameFromEntity(T entity) {
        try {
            var field = entity.getClass().getDeclaredField("name");
            field.setAccessible(true);
            Object value = field.get(entity);
            return value != null ? value.toString() : "";
        } catch (Exception e) {
            return "";
        }
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
