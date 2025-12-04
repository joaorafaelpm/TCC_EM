package com.pendezzapizza.pendezzapizza_api.infrastructure.repository;

import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
import com.pendezzapizza.pendezzapizza_api.domain.repository.ProductRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryQueries {

    @PersistenceContext
    EntityManager manager ;

    @Transactional
    @Override
    public ProductPhoto save (ProductPhoto foto) {
        return manager.merge(foto);
    }

    @Transactional
    @Override
    public void delete (ProductPhoto foto) {
        manager.remove(foto);
    }



}
