# Arquitetura do backend

## Visão Geral

Este projeto utiliza uma arquitetura padrão no backend usando DDD. A ideia central é manter o código bem organizado, fácil de testar e simples de manter.

## Princípios Fundamentais

A arquitetura se baseia em três camadas principais:

Cada camada tem sua própria responsabilidade e não interfere nas outras. As camadas externas dependem das internas, nunca o contrário. A lógica de negócio fica isolada de frameworks e bibliotecas, o que torna o código mais testável e menos acoplado a tecnologias específicas.

## Estrutura de Camadas

A aplicação está dividida em três camadas principais:

**Camada de Apresentação (Presentation)**  
Responsável por receber requisições HTTP através dos controllers, validar dados de entrada e devolver respostas em formato JSON.

**Camada de Domínio (Domain)**  
O núcleo da aplicação, contendo todas as regras de negócio, entidades e lógica central. Esta camada não depende de nenhuma tecnologia específica.

**Camada de Infraestrutura (Infrastructure)**  
Implementa algumas configurações de sistema e integrações com recursos externos.

## Organização de Pacotes

A estrutura de diretórios reflete a separação em camadas. Aqui está como o código está organizado:

### api (Camada da api)

Contém os controllers REST que recebem requisições HTTP, os DTOs para transferência de dados, mappers para conversão entre DTOs e objetos de domínio, e tratamento de exceções com o padrão RFC 7807.

### Domain (Camada de Domínio)

O coração da aplicação. Contém as entidades de negócio, serviços de domínio com lógica de negócio, interfaces de repositório e exceções de negócio.

### Infrastructure (Camada de Infraestrutura)

Implementa os detalhes técnicos: implementações JPA personalizadas, configurações da aplicação.

### Core (Camada de Configuração)

Implementa algumas configurações que não fazem parte do modelo de entidades: configuração do CORS, configuração do serviço de email, padronização da resposta JSON , anotações personalizadas e etc

## Fluxo de Dados

Quando uma requisição chega na aplicação, ela percorre um caminho definido entre as camadas. Por exemplo, ao fazer um pedido:

1. O cliente faz uma requisição HTTP POST
2. O OrderController recebe os dados de entrada
3. O OrderMapper converte o objeto de requisição em um objeto de domínio
4. O OrderIssuanceService valida as regras de negócio 
5. A interface OrderRepository é chamada e nós perssistimos a entidade no banco
6. Depois de perssistida, um evento é lançado e o cliente recebe um email avisando sobre a confirmação do pedido
7. A resposta segue o caminho inverso: após ser perssistida no banco, de um objeto de domínio se torna um modelo de representação, e finalmente JSON para o cliente

## Responsabilidades por Camada

### Api Layer

Esta camada lida com a interface externa da aplicação, especificamente a API REST. Os controllers recebem requisições HTTP e delegam o trabalho para os casos de uso. Os DTOs são usados para transferir dados entre o cliente e a aplicação. Os mappers fazem a conversão entre DTOs e objetos de domínio. As validações garantem que os dados de entrada estão no formato correto.

Tecnologias principais: Spring Web, Bean Validation, MapStruct

### Domain Layer

O núcleo da aplicação onde vive toda a lógica de negócio. As entidades representam conceitos importantes do negócio como Restaurant e User. As interfaces de repositório definem contratos de persistência sem depender de implementação. As classes de serviço implementam as regras de negócio. Classes de evento para auxiliar no fluxo da aplicação e notificação do cliente

### Infrastructure Layer

Implementa todos os detalhes técnicos. Se trata da configuração do serviço de email. E a implementação de alguma interface se necessário

## Exemplos Práticos

### 1. Entidade de Domínio

```java
package com.pendezzapizza.pendezzafood_api.domain.model;

import com.pendezzapizza.pendezzafood_api.domain.event.CancelOrderEvent;
import com.pendezzapizza.pendezzafood_api.domain.event.ConfirmOrderEvent;
import com.pendezzapizza.pendezzafood_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzafood_api.domain.model.enuns.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true , callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractAggregateRoot<Order> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id ;

    private String code;

    private BigDecimal subtotal ;
    private BigDecimal feeShipping ;
    private BigDecimal totalCost ;

    @CreationTimestamp
    private OffsetDateTime creationDate;
    private OffsetDateTime confirmDate ;
    private OffsetDateTime cancelDate;
    private OffsetDateTime deliveryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status" , nullable = false)
    private OrderStatus orderStatus = OrderStatus.CREATED ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private FormaPagamento paymentMethods ;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(nullable = false , name = "client_user_id")
    private User cliente ;

    @Embedded
    private Adress deliveryAdress ;

    @OneToMany(mappedBy = "order" , cascade = CascadeType.ALL)
    private List<OrderItem> itens = new ArrayList<>();

    public void calculateTotalOrderCost() {
        getItens().forEach(OrderItem::setTotalPrice);

        this.subtotal = getItens().stream()
                .map(OrderItem::getPrecoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalCost = this.subtotal.add(this.feeShipping);
    }


    public void confirmar () {
        setOrderStatus(OrderStatus.CONFIRMED);
        setConfirmationDate(OffsetDateTime.now());

        registerEvent(new ConfirmOrderEvent(this));
    }
    public void entregar () {
        setOrderStatus(OrderStatus.DELIVERED);
        setDeliveryDate(OffsetDateTime.now());
    }
    public void cancelar () {
        setOrderStatus(OrderStatus.CANCELED);
        setCancelDate(OffsetDateTime.now());

        registerEvent(new OrderCanceledEvent(this));
    }

    public boolean canBeConfirmed() {
        return getOrderStatus().canAlterTo(OrderStatus.CONFIRMED);
    }
    public boolean canBeDelivered() {
        return getOrderStatus().canAlterTo(OrderStatus.DELIVERED);
    }
    public boolean canBeCanceled() {
        return getOrderStatus().canAlterTo(OrderStatus.CANCELED);
    }

    private void setOrderStatus (OrderStatus newStatus){
        if (getOrderStatus().cantAlterTo(newStatus)) {
            throw new NegocioException(String.format(
                "Order status '%s' cannot be altered from '%s' to '%s'" ,
                    getCodigo(), getOrderStatus().getDescription() , newStatus.getDescription()
            ));
        }

        this.orderStatus = newStatus;
    }

    @PrePersist
    private void generateCode () {
        setCode(UUID.randomUUID().toString());
    }

}

## 2. Domain Service -- OrderEmissionService

``` java
package com.pendezzapizza.pendezzafood_api.v1.domain.service;

import com.pendezzapizza.pendezzafood_api.v1.domain.exception.BusinessException;
import com.pendezzapizza.pendezzafood_api.v1.domain.model.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderEmissionService {

    private final OrderRegisterService orderService;
    private final RestaurantRegisterService restaurantService;
    private final UserRegisterService userService;
    private final PaymentMethodRegisterService paymentMethodService;
    private final ProductRegisterService productService;
    private final CityRegisterService cityService;

    @Transactional
    public Order emitOrder(Order order) {
        assignRelationalObjects(order);
        assignUnitPriceAndProductToItems(order);

        order.setFeeShipping(order.getRestaurant().getShippingFee());
        order.calculateTotalOrderCost();

        return orderService.save(order);
    }

    public void assignRelationalObjects(Order order) {
        Long restaurantId = order.getRestaurant().getId();
        Long paymentMethodId = order.getPaymentMethods().getId();
        Long cityId = order.getDeliveryAdress().getCity().getId();

        City city = cityService.findById(cityId);
        Restaurant restaurant = restaurantService.findById(restaurantId);
        PaymentMethod paymentMethod = paymentMethodService.findById(paymentMethodId);

        Long userId = order.getCliente().getId();
        User user = userService.findById(userId);

        order.getDeliveryAdress().setCity(city);
        order.setRestaurant(restaurant);
        order.setCliente(user);

        if (restaurant.doesNotAcceptPaymentMethod(paymentMethod)) {
            throw new BusinessException(
                String.format("Payment method '%s' is not accepted by this restaurant.",
                        paymentMethod.getDescription()));
        }

        order.setPaymentMethods(paymentMethod);
    }

    public void assignUnitPriceAndProductToItems(Order order) {
        order.getItens().forEach(item -> {
            Product product = productService.findById(
                order.getRestaurant().getId(),
                item.getProduct().getId()
            );

            item.setOrder(order);
            item.setProduct(product);
            item.setUnitPrice(product.getPrice());
        });
    }
}
```

## 3. Domain Event Listener -- CustomerOrderConfirmedNotification

``` java
package com.pendezzapizza.pendezzafood_api.v1.domain.listener;

import com.pendezzapizza.pendezzafood_api.v1.domain.event.OrderConfirmedEvent;
import com.pendezzapizza.pendezzafood_api.v1.domain.model.Order;
import com.pendezzapizza.pendezzafood_api.v1.domain.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CustomerOrderConfirmedNotification {

    @Autowired
    private EmailService emailService;

    @TransactionalEventListener
    private void onOrderConfirmed(OrderConfirmedEvent event) {
        Order order = event.getOrder();

        var message = EmailService.Message.builder()
                .subject(order.getRestaurant().getName() + " - Order Confirmed")
                .body("order-confirmed.html")
                .variable("order", order)
                .recipient(order.getCliente().getEmail())
                .build();

        emailService.send(message);
    }
}
```

## 4. Controller -- OrderController

``` java
package com.pendezzapizza.pendezzafood_api.api.v1.controller;

import com.pendezzapizza.pendezzafood_api.api.v1.assembler.OrderModelAssembler;
import com.pendezzapizza.pendezzafood_api.api.v1.assembler.OrderSummaryModelAssembler;
import com.pendezzapizza.pendezzafood_api.api.v1.assembler.disassembler.OrderDisassembler;
import com.pendezzapizza.pendezzafood_api.api.v1.model.OrderModel;
import com.pendezzapizza.pendezzafood_api.api.v1.model.OrderSummaryModel;
import com.pendezzapizza.pendezzafood_api.api.v1.model.dto.OrderDTO;
import com.pendezzapizza.pendezzafood_api.core.data.PageWrapper;
import com.pendezzapizza.pendezzafood_api.core.data.PageableTranslator;
import com.pendezzapizza.pendezzafood_api.domain.exception.EntityNotFoundException;
import com.pendezzapizza.pendezzafood_api.domain.exception.BusinessException;
import com.pendezzapizza.pendezzafood_api.domain.model.Order;
import com.pendezzapizza.pendezzafood_api.domain.filter.OrderFilter;
import com.pendezzapizza.pendezzafood_api.domain.service.OrderRegisterService;
import com.pendezzapizza.pendezzafood_api.domain.service.OrderEmissionService;
import com.pendezzapizza.pendezzafood_api.infrastructure.repository.spec.OrderSpecs;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/orders")
@AllArgsConstructor
public class OrderController {

    private OrderRegisterService orderService;
    private OrderEmissionService orderEmissionService;

    private OrderModelAssembler orderModelAssembler;
    private OrderSummaryModelAssembler orderSummaryModelAssembler;
    private OrderDisassembler orderDisassembler;
    private PagedResourcesAssembler<Order> pagedResourcesAssembler;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderModel save(@RequestBody @Valid OrderDTO dto) {
        try {
            Order order = orderDisassembler.toOrder(dto);
            return orderModelAssembler.toModel(orderEmissionService.emitOrder(order));
        } catch (EntityNotFoundException e) {
            throw new BusinessException(e.getMessage(), e);
        }
    }

}
```