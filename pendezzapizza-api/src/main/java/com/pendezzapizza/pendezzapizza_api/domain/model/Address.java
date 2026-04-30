/**
 * @summary     Representa um endereço incorporável que pode ser embutido em outras entidades JPA.
 *              Contém os campos de logradouro, CEP, número, complemento, bairro e cidade.
 * @difficulty  Low
 * @depends-on  City
 */
package com.pendezzapizza.pendezzapizza_api.domain.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Componente de endereço reutilizável, mapeado diretamente nas colunas
 * da tabela da entidade dona (ex: tabela de clientes ou restaurantes).
 * Não possui tabela própria no banco de dados.
 */
// Ser embeddable significa que ele faz parte de alguma coisa e nunca vai ser único (root)
@Embeddable
@Data
public class Address {

    @Column(name = "address_zipCode")
    private String zipCode;

    @Column(name = "address_street")
    private String street;

    @Column(name = "address_number")
    private String number;

    /** Campo opcional — pode ser nulo quando o endereço não possui complemento. */
    @Column(name = "address_complement")
    private String complement;

    @Column(name = "address_neighborhood")
    private String neighborhood;

    /**
     * Objeto de cidade associado a este endereço.
     * O {@code @ManyToOne} indica que muitos endereços podem apontar para a mesma cidade.
     * O {@code fetch = FetchType.LAZY} significa que os dados da cidade só são carregados
     * do banco quando explicitamente acessados, evitando consultas desnecessárias.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_city_id")
    private City city;

}