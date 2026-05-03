/**
 * @summary     Representa o modelo de resposta completo de um produto de restaurante retornado pela API.
 *              Inclui dados de identificação, precificação e disponibilidade do produto.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ProductModel {

    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID id;

    // ID do restaurante dono do produto — exposto para facilitar navegação e filtragem no cliente
    // sem necessidade de inferir o contexto pela URL da requisição
    @Schema(example = "943af7ca-3ae8-41fa-a1b0-5cd1d9f82e48")
    private UUID restaurantId;

    @Schema(example = "Fettuccine Alfredo")
    private String name;

    @Schema(example = "Fettuccine artesanal com molho cremoso de manteiga e parmesão de 24 meses.")
    private String description;

    // Preço atual do produto — representa o valor vigente no catálogo, não o valor histórico de pedidos
    @Schema(example = "45.50")
    private BigDecimal price;

    // Controla visibilidade do produto no cardápio — produtos inativos não aparecem para o cliente final
    @Schema(example = "true")
    private Boolean active;
}