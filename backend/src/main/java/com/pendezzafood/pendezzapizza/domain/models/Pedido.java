package com.pendezzafood.pendezzapizza.domain.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.pendezzafood.pendezzapizza.domain.models.enuns.StatusPedido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @EqualsAndHashCode.Include
    @Column(columnDefinition = "BINARY(16)")
    private UUID id = UUID.randomUUID() ;

    private BigDecimal subTotal ;
    private BigDecimal taxaFrete ;
    private BigDecimal valorTotal ;

    @CreationTimestamp
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConfirmacao ;
    private LocalDateTime dataCancelamento;
    private LocalDateTime dataEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido")
    private StatusPedido statusPedido = StatusPedido.CRIADO ;

    @ManyToOne
    @JoinColumn(nullable = false)
    private FormaPagamento formaPagamento ;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Restaurante restaurante;

    @ManyToOne
    @JoinColumn(nullable = false , name = "cliente_usuario_id")
    private Usuario cliente ;

    @Embedded
    private Endereco enderecoEntrega ;

    @OneToMany(mappedBy = "pedido" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<ItemPedido> itens = new ArrayList<>();



}

