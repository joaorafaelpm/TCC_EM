package com.pendezzapizza.pendezzapizza_api.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Repositório personalizado com funções comuns a todos os outros
 */
@NoRepositoryBean
public interface CustomJPARepository<T , ID> extends JpaRepository<T , ID> {

//    Suas documentações estão nas suas implementações, mas o nome é auto explicativo
    Page<T> findByName(String name, Pageable pageable);
    T findByIdOrThrowException(UUID id) ;
    T findByIdOrThrowException(Object foreignEntity , UUID entityId , UUID foreignId) ;

}
