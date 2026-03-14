package com.pendezzapizza.pendezzapizza_api.api.v1.model.DTO;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AddressDTO {

    @NotBlank
    private String cep ;

    @NotBlank
    private String street ;
    @NotBlank
    private String number ;

    private String complement ;

    @NotBlank
    private String neighbourhood ;

    @NotNull
    @Valid
    private CityIdDTO city ;


}
