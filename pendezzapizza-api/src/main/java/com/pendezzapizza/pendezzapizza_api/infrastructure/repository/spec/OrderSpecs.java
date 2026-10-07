package com.pendezzapizza.pendezzapizza_api.infrastructure.repository.spec;

import com.pendezzapizza.pendezzapizza_api.domain.filter.OrderFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

/**
 * Specs para a classe de {@code Order}
 *
 * <p>Usamos as specs para filtrar a pesquisa de pedidos</p>
 */
public class OrderSpecs {

//    Filtro
    public static Specification<Order> withFilter (OrderFilter filter) {
        return (root,query,builder) -> {
//            Primeiro a gente busca o usuário para evitar NullPointerException
            if (Order.class.equals(query.getResultType())) {
                root.fetch("customer");
            }

//            Criamos a lista de predicados (necessário para continuar), entenda predicados como os filtros
            var predicates = new ArrayList<Predicate>() ;

//            Filtramos por id de usuário
            if (filter.getCustomerId() != null) {
                predicates.add(builder.equal(root.get("customer").get("id") , filter.getCustomerId()));
            }

//            Filtramos por id de restaurante
            if(filter.getRestaurantId() != null) {
                predicates.add(builder.equal(root.get("restaurant").get("id") , filter.getRestaurantId()));
            }

//            Filtramos pelo inicio da data de criação do pedido
            if (filter.getStartCreationDate() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("creationDate") ,
                        filter.getStartCreationDate()));
            }
//            Filtramos pelo fim da data de criação do pedido
            if (filter.getEndCreationDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("creationDate") ,
                        filter.getEndCreationDate()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
