package com.pendezzapizza.pendezzapizza_api.infrastructure.repository.spec;

import com.pendezzapizza.pendezzapizza_api.domain.filter.OrderFilter;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class OrderSpecs {

    public static Specification<Order> withFilter (OrderFilter filter) {
        return (root,query,builder) -> {
            if (Order.class.equals(query.getResultType())) {
                root.fetch("customer");
            }

            var predicates = new ArrayList<Predicate>() ;

            if (filter.getCustomerId() != null) {
                predicates.add(builder.equal(root.get("customer").get("id") , filter.getCustomerId()));
            }

            if(filter.getRestaurantId() != null) {
                predicates.add(builder.equal(root.get("restaurant").get("id") , filter.getCustomerId()));
            }

            if (filter.getStartCreationDate() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("creationDate") ,
                        filter.getStartCreationDate()));
            }
            if (filter.getEndCreationDate() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("creationDate") ,
                        filter.getEndCreationDate()));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
