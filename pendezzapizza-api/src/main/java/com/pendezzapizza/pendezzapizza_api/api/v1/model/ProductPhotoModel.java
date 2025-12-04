package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(collectionRelation = "productPhotos")
@Getter
@Setter
public class ProductPhotoModel extends RepresentationModel<ProductPhotoModel> {


    private String fileName ;
    private String description ;
    private String contentType ;
    private Long size ;

}
