package com.pendezzapizza.pendezzapizza_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Essa classe serve simplesmente para servir de um modelo de erro para a minha classe de OrderBatch
 * Serve para armazenar mais de um erro, que contem o index do erro junto de sua mensagem
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderBatchErrorModel {
    int index;
    String message;
}
