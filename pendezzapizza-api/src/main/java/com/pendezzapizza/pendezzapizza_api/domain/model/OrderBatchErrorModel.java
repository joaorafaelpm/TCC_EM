package com.pendezzapizza.pendezzapizza_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderBatchErrorModel {
    int index;
    String message;
}
