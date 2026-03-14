package com.pendezzapizza.pendezzapizza_api.domain.repository;


import com.pendezzapizza.pendezzapizza_api.domain.model.ProductPhoto;

public interface ProductRepositoryQueries {

    ProductPhoto save (ProductPhoto foto) ;
    void delete (ProductPhoto foto) ;

}
