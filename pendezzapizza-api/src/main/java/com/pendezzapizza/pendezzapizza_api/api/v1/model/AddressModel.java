package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddressModel {

    private String zipCode ;
    private String street ;
    private String number ;
    private String complement ;
    private String neighbourhood ;
    private CityModel city;


}
