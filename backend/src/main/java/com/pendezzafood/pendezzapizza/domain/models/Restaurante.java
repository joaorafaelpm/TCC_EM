package com.pendezzafood.pendezzapizza.domain.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String nome ;

    @JoinColumn(name = "taxa_frete" , nullable = false)
    private BigDecimal taxaFrete;

//    Isso significa que a classe Endereço faz parte de restaurante e vice-versa
    @Embedded
    @JsonIgnore
    private Endereco endereco;

    @CreationTimestamp
    @Column(columnDefinition = "datetime" , name = "data_cadastro")
//    @JsonIgnore
    private LocalDateTime dataCadastro ;

    @UpdateTimestamp
    @Column(columnDefinition = "datetime" , name = "data_atualizacao" , nullable = false)
//    @JsonIgnore
    private LocalDateTime dataAtualizacao;

    @JsonIgnore
    @OneToMany(mappedBy = "restaurante" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<Produto> produtos = new ArrayList<>();


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento" ,
            joinColumns = @JoinColumn(name = "restaurante_id") ,
            inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
    private List<FormaPagamento> formasPagamento = new ArrayList<>();

    public Restaurante (String nome , BigDecimal taxaFrete) {
        this.nome = nome ;
        this.taxaFrete = taxaFrete ;
    }


}
