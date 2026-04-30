package com.pendezzapizza.pendezzapizza_api.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface CustomJPARepository<T , ID> extends JpaRepository<T , ID> {


    Page<T> findByName(String name, Pageable pageable);
    T findByIdOrThrowException(UUID id) ;
    T findByIdOrThrowException(Object foreignEntity , UUID entityId , UUID foreignId) ;

    void detach(T entity) ;
}
