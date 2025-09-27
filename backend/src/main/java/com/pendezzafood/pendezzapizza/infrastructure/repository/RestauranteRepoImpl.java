package com.pendezzafood.pendezzapizza.infrastructure.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.pendezzafood.pendezzapizza.domain.models.Restaurante;
import com.pendezzafood.pendezzapizza.domain.repositories.RestauranteRepo;
import com.pendezzafood.pendezzapizza.domain.repositories.RestauranteRepoQueries;
import com.pendezzafood.pendezzapizza.infrastructure.repository.spec.RestauranteSpecs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RestauranteRepoImpl implements RestauranteRepoQueries{

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    @Lazy
    private RestauranteRepo restauranteRepository;

    @Override
    public List<Restaurante> find(String nome , BigDecimal taxaInicial , BigDecimal taxaFinal) {

//        Inicia a "fabrica" do criteria
        CriteriaBuilder builder = manager.getCriteriaBuilder() ;

//        Instancia um novo query para fazer o JPQL personalizado
        CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class) ;

//        Pega a instância do objeto que estamos trabalhando, nesse caso, ele pode acessar as informações da classe de restaurante
        Root<Restaurante> root = criteria.from(Restaurante.class);

        var predicates = new ArrayList<Predicate>();

//        Criamos os predicados para passar de parâmetro no where
        if (StringUtils.hasLength(nome)) {
            Predicate nomePredicate = builder
                    .like(root.get("nome") , "%" + nome + "%");
            predicates.add(nomePredicate);
        }
        if (taxaInicial != null) {
            predicates.add(builder
                    .greaterThanOrEqualTo(root.get("taxaFrete") , taxaInicial));
        }
        if(taxaFinal != null) {
            predicates.add(builder
                    .lessThanOrEqualTo(root.get("taxaFrete") , taxaFinal));
        }

//        Recebe predicados que são os parâmetros que passamos junto do where no JPQL (like, >= , <=)
        criteria.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Restaurante> query = manager.createQuery(criteria) ;
        return query.getResultList();

    }

    @Override
    public List<Restaurante> findFreteGratisPorNome (String nome) {
        return restauranteRepository.
                findAll(RestauranteSpecs.comFreteGratis().and(RestauranteSpecs.comNomeSemelhante(nome))) ;
    }

}