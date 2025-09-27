package com.pendezzafood.pendezzapizza.domain.models;

import jakarta.persistence.*;
import lombok.Data;

@Embeddable
@Data
//Indica que a classe é encorporada a outra, e não uma tabela em sí
public class Endereco {

    @Column(name = "endereco_cep")
    private String cep ;
    @Column(name = "endereco_logradouro")
    private String logradouro ;
    @Column(name = "endereco_numero")
    private String numero ;
    @Column(name = "endereco_complemento")
    private String complemento ;
    @Column(name = "endereco_bairro")
    private String bairro ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endereco_cidade_id")
    private Cidade cidade ;

}
