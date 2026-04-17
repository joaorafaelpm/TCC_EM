package com.pendezzapizza.pendezzapizza_api.infrastructure.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.Restaurant;
import com.pendezzapizza.pendezzapizza_api.domain.model.RestaurantPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RestaurantRepository;
import com.pendezzapizza.pendezzapizza_api.domain.repository.RestaurantRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.pendezzapizza.pendezzapizza_api.infrastructure.repository.spec.RestaurantSpecs.withFreeShippingFee;
import static com.pendezzapizza.pendezzapizza_api.infrastructure.repository.spec.RestaurantSpecs.withSimilarName;

@Repository
public class RestaurantRepositoryImpl implements RestaurantRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    @Lazy
    private RestaurantRepository restaurantRepository;

//    Antes do find eu já resolvo os problemas da foto
    @Transactional
    @Override
    public RestaurantPhoto savePhoto(RestaurantPhoto foto) {
        return manager.merge(foto);
    }

    @Transactional
    @Override
    public void deletePhoto(RestaurantPhoto foto) {
        manager.remove(foto);
    }

    @Override
    public List<Restaurant> find(String nome , BigDecimal taxaInicial , BigDecimal taxaFinal) {

//        Inicia a "fabrica" do criteria
        CriteriaBuilder builder = manager.getCriteriaBuilder() ;

//        Instancia um novo query para fazer o JPQL personalizado
        CriteriaQuery<Restaurant> criteria = builder.createQuery(Restaurant.class) ;

//        Pega a instância do objeto que estamos trabalhando, nesse caso, ele pode acessar as informações da classe de restaurante
        Root<Restaurant> root = criteria.from(Restaurant.class);

        var predicates = new ArrayList<Predicate>();

//        Criamos os predicados para passar de parâmetro no where
        if (StringUtils.hasLength(nome)) {
            Predicate nomePredicate = builder
                    .like(root.get("name") , "%" + nome + "%");
            predicates.add(nomePredicate);
        }
        if (taxaInicial != null) {
            predicates.add(builder
                    .greaterThanOrEqualTo(root.get("shippingFee") , taxaInicial));
        }
        if(taxaFinal != null) {
            predicates.add(builder
                    .lessThanOrEqualTo(root.get("shippingFee") , taxaFinal));
        }

//        Recebe predicados que são os parâmetros que passamos junto do where no JPQL (like, >= , <=)
        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Restaurant> query = manager.createQuery(criteria) ;
        return query.getResultList();

    }

    @Override
    public List<Restaurant> findFreeShippingFeeByName (String nome) {
        return restaurantRepository.
                findAll(withFreeShippingFee().and(withSimilarName(nome))) ;
    }

}
