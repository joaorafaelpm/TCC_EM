package com.pendezzapizza.pendezzapizza_api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface CustomJPARepository<T , ID> extends JpaRepository<T , ID> {

    Optional<T> findFirst() ;

    T findByIdOrThrowException(UUID id) ;
    T findByIdOrThrowException(Object foreignEntity , UUID entityId , UUID foreignId) ;

    void detach(T entity) ;
}
