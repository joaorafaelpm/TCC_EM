package com.pendezzafood.pendezzapizza.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pendezzafood.pendezzapizza.domain.models.FormaPagamento;

@Repository
public interface FormaPagamentoRepo extends JpaRepository<FormaPagamento , Long> {



}
