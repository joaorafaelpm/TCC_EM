package com.pendezzapizza.pendezzapizza_api.domain.repository;


import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;
/**
 * Interface criada para fazer algumas funções extras da entidade de <b>produto</b>
 *
 * <p>Toda query sql a mais está implementada dentro da função com seu código sql explicado em sua implementação</p>
 */
public interface ProductRepositoryQueries {

    ProductPhoto save (ProductPhoto foto) ;
    void delete (ProductPhoto foto) ;

}
