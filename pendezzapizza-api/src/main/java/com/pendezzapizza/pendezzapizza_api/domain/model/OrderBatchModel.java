package com.pendezzapizza.pendezzapizza_api.domain.model;

import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Modelo de OrderBatch, aqui dentro a gente recebe uma lista dos pedidos criados e posteriormente os erros de cada um
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderBatchModel {
    List<OrderModel> created;
    List<OrderBatchErrorModel> errors;
}
