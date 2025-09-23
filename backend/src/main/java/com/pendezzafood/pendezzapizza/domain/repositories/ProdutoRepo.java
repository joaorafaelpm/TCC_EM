package com.pendezzafood.pendezzapizza.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pendezzafood.pendezzapizza.domain.models.Produto;

@Repository
public interface ProdutoRepo extends JpaRepository<Produto , Long> {
}
