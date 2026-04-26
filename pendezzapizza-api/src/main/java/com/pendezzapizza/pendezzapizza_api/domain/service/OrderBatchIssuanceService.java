package com.pendezzapizza.pendezzapizza_api.domain.service;

import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.OrderModelAssembler;
import com.pendezzapizza.pendezzapizza_api.api.v1.assembler.disassambler.OrderDisassembler;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderBatchErrorModel;
import com.pendezzapizza.pendezzapizza_api.domain.model.OrderBatchModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzapizza_api.api.v1.model.dto.OrderDTO;
import com.pendezzapizza.pendezzapizza_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzapizza_api.domain.model.Order;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderBatchIssuanceService {

    private final OrderIssuanceService orderIssuanceService;
    private final OrderDisassembler orderDisassembler;
    private final OrderModelAssembler orderModelAssembler;

    // Sem @Transactional aqui — cada pedido tem sua própria transação
    public OrderBatchModel issueBatch(List<OrderDTO> orderDTOs) {
        List<OrderModel> created = new ArrayList<>();
        List<OrderBatchErrorModel> errors = new ArrayList<>();

        for (int i = 0; i < orderDTOs.size(); i++) {
            try {
                Order order = orderDisassembler.orderDTOToOrder(orderDTOs.get(i));
                OrderModel saved = orderModelAssembler.toModel(orderIssuanceService.issueOrder(order));
                created.add(saved);
            } catch (EntityNotFoundException | BusinessException e) {
                errors.add(new OrderBatchErrorModel(i, e.getMessage()));
            }
        }

        return new OrderBatchModel(created, errors);
    }
}