/**
 * @summary     Representa o modelo de resposta completo de um pedido retornado pela API.
 *              Agrega dados financeiros, timestamps do ciclo de vida, partes envolvidas e itens do pedido.
 * @difficulty  Low
 * @depends-on  None
 */
package com.pendezzapizza.pendezzapizza_api.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrderModel {

    @Schema(example = "936dc9ec-05bf-44e5-8c07-7e51adc6083d")
    private UUID id;

    // Soma dos preços dos itens, sem taxa de entrega
    @Schema(example = "298.90")
    private BigDecimal subtotal;

    @Schema(example = "10.00")
    private BigDecimal shippingFee;

    // Valor final do pedido: subtotal + shippingFee — pré-calculado e persistido,
    // refletindo os preços vigentes no momento da criação do pedido
    @Schema(example = "308.90")
    private BigDecimal totalValue;

    @Schema(example = "2022-12-01T20:34:04Z")
    private OffsetDateTime createdAt;

    // Nulo enquanto o pedido não for confirmado pelo restaurante
    @Schema(example = "2022-12-01T20:35:10Z")
    private OffsetDateTime confirmedAt;

    // Nulo até a entrega ser registrada; preenchido independentemente do status de cancelamento
    @Schema(example = "2022-12-01T20:55:30Z")
    private OffsetDateTime deliveredAt;

    // Nulo enquanto o pedido estiver ativo; mutuamente exclusivo com deliveredAt na prática,
    // mas ambos os campos coexistem no modelo sem constraint explícita
    @Schema(example = "2022-12-01T20:35:00Z")
    private OffsetDateTime canceledAt;

    // Valores possíveis determinados pelo ciclo de vida do pedido no domínio (ex: CRIADO, CONFIRMADO, ENTREGUE, CANCELADO)
    @Schema(example = "CRIADO")
    private String status;

    // Restaurante representado como resumo para reduzir o payload — dados completos disponíveis via endpoint de restaurante
    private RestaurantSummaryModel restaurant;

    private UserModel customer;
    private AddressModel deliveryAddress;
    private PaymentMethodModel paymentMethod;
    private List<OrderItemModel> items;
}